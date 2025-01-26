package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.person.PeopleFilteringResponseDTO;
import com.pawelnu.BackendProjectManager.dto.person.PersonCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.person.PersonDTO;
import com.pawelnu.BackendProjectManager.dto.person.PersonFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.enums.PersonRole;
import java.util.UUID;

public interface IPersonService {

  PeopleFilteringResponseDTO getAllPeople(
      Integer pageNumber, Integer pageSize, String field, Boolean isAscendingSorting);

  PersonDTO getPersonById(UUID id);

  PersonDTO getPersonByName(String name);

  PersonDTO createPerson(PersonCreateRequestDTO personCreateRequestDTO);

  String deletePersonById(UUID id);

  PeopleFilteringResponseDTO searchProject(PersonFilteringRequestDTO personFilteringRequestDTO);

  PersonDTO changePersonRole(PersonRole personRole);
}
