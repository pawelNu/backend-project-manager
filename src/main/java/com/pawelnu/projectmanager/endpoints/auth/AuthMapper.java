package com.pawelnu.projectmanager.endpoints.auth;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  default PermissionDTO stringToPermission(String authority) {
    if (authority == null || !authority.contains("_")) {
      return null;
    }

    int index = authority.lastIndexOf("_");
    String resource = authority.substring(0, index);
    String action = authority.substring(index + 1);

    return PermissionDTO.builder().resource(resource.replace("_", "-")).action(action).build();
  }

  default List<PermissionDTO> toPermissions(List<String> frontendAuthorities) {
    return frontendAuthorities.stream().map(this::stringToPermission).collect(Collectors.toList());
  }
}
