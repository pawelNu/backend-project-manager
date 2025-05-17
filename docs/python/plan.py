from dataclasses import dataclass, field
from datetime import datetime
from enum import Enum
from typing import List, Optional, Set
from uuid import UUID

from utils import ApiModel, Endpoint, RequestBody, ResponseBody


@dataclass(kw_only=True)
class Auditable:
    version: int = 0
    created: datetime = field(default_factory=datetime.now)
    lastModified: datetime = field(default_factory=datetime.now)
    isDeleted: bool = False


@dataclass
class CompanyEntity(Auditable):
    id: UUID
    name: str
    nip: str
    regon: str
    website: str
    status: "CompanyStatus"
    addresses: List["CompanyAddressEntity"]


class CompanyStatus(Enum):
    ACTIVE = "Active"
    TERMINATED = "Terminated"


@dataclass
class CompanyAddressEntity(Auditable):
    id: UUID
    company: CompanyEntity
    street: str
    streetNumber: str
    city: str
    zipCode: str
    country: str
    phoneNumber: str
    emailAddress: str
    addressType: str


@dataclass
class AuthorityEntity(Auditable):
    id: UUID
    name: str
    employees: Set["EmployeeAuthorityEntity"]


@dataclass
class EmployeeEntity(Auditable):
    id: UUID
    first_name: str
    last_name: str
    username: str
    password: str
    email: str
    phone_number: str
    company: CompanyEntity
    authorities: Set["EmployeeAuthorityEntity"]


@dataclass
class EmployeeAuthorityEntity(Auditable):
    id: UUID
    employee: EmployeeEntity
    authority: AuthorityEntity


@dataclass
class ProjectEntity(Auditable):
    id: UUID
    name: str
    type: "ProjectTypeEntity"
    steps: List["ProjectStepEntity"]


@dataclass
class ProjectTypeEntity(Auditable):
    id: UUID
    name: str

@dataclass
class ProjectStepEntity(Auditable):
    id: UUID
    name: str
    priority: "PriorityEntity"
    comments: List["CommentProjectStepEntity"]
    tickets: List["TicketEntity"]
    deadline: datetime

@dataclass
class CommentProjectStepEntity(Auditable):
    id: UUID
    name: str
    tickets: List["TicketEntity"]
    deadline: datetime

@dataclass
class PriorityEntity(Auditable):
    id: UUID
    name: str

@dataclass
class TicketEntity(Auditable):
    id: UUID
    ticket_number: str
    status: "TicketStatusEntity"

@dataclass
class TicketStatusEntity(Auditable):
    id: UUID
    name: str

@dataclass
class User:
    id: UUID
    username: str
    email: str
    age: Optional[int]


@dataclass
class UserCreateRequest:
    username: str
    email: str
    age: int


@dataclass
class UserResponse:
    id: int
    username: str
    email: str


company_api = ApiModel(model=CompanyEntity)
company_address_api = ApiModel(model=CompanyAddressEntity)
employee_api = ApiModel(model=EmployeeEntity)
employee_authority_api = ApiModel(model=EmployeeAuthorityEntity)
project_api = ApiModel(model=ProjectEntity)
project_type_api = ApiModel(model=ProjectTypeEntity)
project_step_api =ApiModel(model=ProjectStepEntity)
priority_api =ApiModel(model=PriorityEntity)
ticket_api =ApiModel(model=TicketEntity)
ticket_status_api =ApiModel(model=TicketStatusEntity)

# company_api.add_endpoint(
#     Endpoint(
#         "/users",
#         "POST",
#         request_headers=["Bearer token"],
#         request_body=RequestBody(UserCreateRequest),
#         response_body=ResponseBody(201, UserResponse),
#     )
# )
# user_api.add_endpoint(
#     Endpoint(
#         "/users/{id}",
#         "GET",
#         path_params=[Field("id", UUID, True)],
#         query_params=[Field("verbose", bool, False)],
#         response_body=None,
#     )
# )

apis: List[ApiModel] = []
apis.append(company_api)
apis.append(company_address_api)
apis.append(employee_api)
apis.append(employee_authority_api)
apis.append(project_api)
apis.append(project_type_api)

for api in apis:
    # print(api)
    print(api.to_str_model())
    # print(api.to_str_endpoints())
