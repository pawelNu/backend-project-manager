package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.config.security.services.UserDetailsDTO;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "id", ignore = true)
  EmployeeEntity toEntity(EmployeeCreateRequestDTO dto);

  @Mapping(target = "id", ignore = true)
  EmployeeEntity toEntity(EmployeeEditRequestDTO body, @MappingTarget EmployeeEntity entity);

  @Mapping(source = "company.name", target = "companyName")
  EmployeeDTO toDTO(EmployeeEntity entity);

  @Mapping(source = "userId", target = "id")
  @Mapping(source = "authorities", target = "authorities")
  UserDetailsDTO toUserDetailsDTO(EmployeeRowDTO entity);

  default Collection<? extends GrantedAuthority> map(List<String> authorities) {
    if (authorities == null) {
      return List.of();
    }

    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  default EmployeeRowDTO toEmployeeRowDTO(List<EmployeeAuthorityRowDTO> rows) {
    List<String> authorities =
        rows.stream()
            .map(EmployeeAuthorityRowDTO::getAuthorityName)
            .filter(Objects::nonNull)
            .toList();
    EmployeeAuthorityRowDTO first = rows.getFirst();
    return EmployeeRowDTO.builder()
        .userId(first.getUserId())
        .username(first.getUsername())
        .email(first.getEmail())
        .password(first.getPassword())
        .authorities(authorities)
        .build();
  }
}
