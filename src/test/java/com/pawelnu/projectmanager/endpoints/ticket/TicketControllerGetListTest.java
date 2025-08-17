package com.pawelnu.projectmanager.endpoints.ticket;

import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Shared.Field;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class TicketControllerGetListTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_TICKETS;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(Postgres.POSTGRES_17)
          .withDatabaseName(Postgres.DB_NAME)
          .withUsername(Postgres.USER)
          .withPassword(Postgres.PASSWORD);

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add(SpringDataSource.URL, postgres::getJdbcUrl);
    registry.add(SpringDataSource.USERNAME, postgres::getUsername);
    registry.add(SpringDataSource.PASSWORD, postgres::getPassword);
  }

  @BeforeEach
  void beforeEach() {
    Utils.generateToken(jwtUtils);
  }

  @Test
  void shouldReturn_200_getTicketList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<TicketDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
    assertEquals(
        UUID.fromString("828acb15-8c4a-4593-9ce6-0a62920de2a7"), responseBody.getFirst().getId());
  }

  @Test
  void shouldReturn_200_getTicketList_withFilters() throws Exception {
    Map<String, String> filter = Map.of(Field.number, "9", Field.title, "and");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<TicketDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals(
        UUID.fromString("6fe31f7d-840f-4767-829e-475f402d16e3"), responseBody.getFirst().getId());
  }

  @Test
  void shouldReturn_200_getTicketList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of(Field.deadline, "DESC");
    Map<String, String> filter = Map.of(Field.title, "and");
    String sortString = objectMapper.writeValueAsString(sort);
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL)
                    .with(withJwt())
                    .param("sort", sortString)
                    .param("filter", filterStrig))
            .andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<TicketDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-3/4", headerContentRange);
    assertEquals(4, responseBody.size());
    assertEquals(
        UUID.fromString("828acb15-8c4a-4593-9ce6-0a62920de2a7"), responseBody.getFirst().getId());
  }

  @Test
  void shouldReturn_200_getTicketList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    List<String> sort = List.of(Field.number, "ASC");
    String rangeString = objectMapper.writeValueAsString(range);
    String sortString = objectMapper.writeValueAsString(sort);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL).with(withJwt()).param("sort", sortString).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<TicketDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals(
        UUID.fromString("5235bd17-661d-4ccb-9b37-effa2aadc462"), responseBody.getFirst().getId());
  }

  @Test
  void shouldReturn_200_getTicketList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of(Field.number, "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<TicketDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getTicketList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getTicketList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }
}
