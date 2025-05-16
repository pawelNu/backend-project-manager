from dataclasses import dataclass, field, fields
from typing import List, Optional, Type, Union, get_type_hints

from http import HTTPStatus


def get_type_name(f_type):
    origin = getattr(f_type, "__origin__", None)
    args = getattr(f_type, "__args__", ())

    if origin is Union:
        non_none_args = [a for a in args if a is not type(None)]
        if len(non_none_args) == 1:
            inner_type = get_type_name(non_none_args[0])
            return f"Optional[{inner_type}]"
        else:
            inner_types = ", ".join(get_type_name(a) for a in args)
            return f"Union[{inner_types}]"
    elif origin:
        # To jest typ generyczny, np. List[User], Dict[str, int], itp.
        inner_types = ", ".join(get_type_name(a) for a in args)
        origin_name = getattr(origin, "__name__", str(origin))
        return f"{origin_name}[{inner_types}]"
    else:
        if hasattr(f_type, "__name__"):
            return f_type.__name__
        else:
            return str(f_type)


@dataclass
class Field:
    name: str
    type: Type
    required: bool

    def __str__(self):
        type_name = getattr(self.type, "__name__", str(self.type))
        required_str = ""
        if self.required:
            required_str = " - (required)"
        return f"{self.name}: {type_name}{required_str}"


@dataclass
class RequestBody:
    model: Type

    def __str__(self):
        result = f"{self.model.__name__} {{\n"
        hints = get_type_hints(self.model)
        for f in fields(self.model):
            f_type = hints.get(f.name, f.type)
            type_name = get_type_name(f_type)
            result += f"  {f.name}: {type_name}\n"
        result += "}"
        return result


@dataclass
class ResponseBody:
    code: int
    model: Optional[Type] = None

    def __str__(self):
        status_name = (
            HTTPStatus(self.code).phrase
            if self.code in HTTPStatus._value2member_map_
            else "Unknown"
        )

        if self.model:
            result = f"{self.code} {status_name}:\n {self.model.__name__} {{\n"
            hints = get_type_hints(self.model)
            for f in fields(self.model):
                f_type = hints.get(f.name, f.type)
                type_name = get_type_name(f_type)
                result += f"  {f.name}: {type_name}\n"
            result += "}"
            return result
        else:
            return f"{self.code} {status_name} No Content"


error_responses: List[ResponseBody] = [
    ResponseBody(400),
    ResponseBody(401),
    ResponseBody(403),
    ResponseBody(404),
    ResponseBody(500),
]


@dataclass
class Endpoint:
    path: str
    method: str
    request_body: Optional[RequestBody] = None
    request_headers: List[str] = field(default_factory=list)
    path_params: List[Field] = field(default_factory=list)
    query_params: List[Field] = field(default_factory=list)
    response_body: ResponseBody = None
    response_headers: List[str] = field(default_factory=list)

    def __str__(self):
        all_responses = [self.response_body] + error_responses
        result = f"\nEndpoint: {self.method} {self.path}\n"
        if self.request_headers:
            result += "  Request headers:\n"
        for h in self.request_headers:
            result += f"    - {h}\n"
        if self.request_body:
            result += f"  Request body:\n   {self.request_body}\n"
        if self.path_params:
            params_str = ", ".join(str(p) for p in self.path_params)
            result += f"  Path params: {params_str}\n"
        if self.query_params:
            params_str = ", ".join(str(p) for p in self.query_params)
            result += f"  Query params: {params_str}\n"
        if self.response_headers:
            result += "  Response headers:\n"
        for h in self.response_headers:
            result += f"    - {h}\n"
        if all_responses:
            result += "  Responses:\n"
            for resp in all_responses:
                if resp is None:
                    result += f"    2xx no response provided\n"
                else:
                    result += f"    {resp}\n"
        return result


@dataclass
class ApiModel:
    model: Optional[Type] = None
    endpoints: List[Endpoint] = field(default_factory=list)

    def add_endpoint(self, endpoint: Endpoint):
        self.endpoints.append(endpoint)

    def __str__(self):
        return self.to_str_full()

    def to_str_model(self) -> str:
        model_name = self.model.__name__ if self.model else "Unknown"
        result = f"Api: {model_name} - Model\n"
        if self.model:
            result += str(RequestBody(self.model)) + "\n"
        else:
            result += "  No model defined\n"
        return result

    def to_str_endpoints(self) -> str:
        model_name = self.model.__name__ if self.model else "Unknown"
        result = f"Api: {model_name} - Endpoints\n"
        if self.endpoints:
            for ep in self.endpoints:
                result += str(ep)
        else:
            result += "  No endpoints defined\n"
        return result

    def to_str_full(self) -> str:
        model_name = self.model.__name__ if self.model else "Unknown"
        result = f"Api: {model_name}\n"
        if self.model:
            result += "Model:\n"
            result += str(RequestBody(self.model)) + "\n"
        for ep in self.endpoints:
            result += str(ep)
        return result
