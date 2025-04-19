package com.pawelnu.projectmanager.service;

import com.pawelnu.projectmanager.dto.person.PeopleFilteringResponseDTO;
import com.pawelnu.projectmanager.dto.person.PersonCreateRequestDTO;
import com.pawelnu.projectmanager.dto.person.PersonDTO;
import com.pawelnu.projectmanager.dto.person.PersonFilteringRequestDTO;
import com.pawelnu.projectmanager.enums.PersonRole;
import java.util.UUID;

public interface PersonService {

  PeopleFilteringResponseDTO getAllPeople(
      Integer pageNumber, Integer pageSize, String field, Boolean isAscendingSorting);

  PersonDTO getPersonById(UUID id);

  PersonDTO getPersonByName(String name);

  PersonDTO createPerson(PersonCreateRequestDTO personCreateRequestDTO);

  String deletePersonById(UUID id);

  PeopleFilteringResponseDTO searchProject(PersonFilteringRequestDTO personFilteringRequestDTO);

  PersonDTO changePersonRole(PersonRole personRole);
}
