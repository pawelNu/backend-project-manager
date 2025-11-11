package com.pawelnu.projectmanager.endpoints.ticket.history;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryDTO;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class TicketControllerCreateTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_TICKET_HISTORIES;

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
  void shouldReturn_201_createTicketHistory() throws Exception {
    var request =
        TicketHistoryCreateRequestDTO.builder()
            .ticketId(UUID.fromString("52f66d4e-79bf-4e85-93f9-405322fe7a4a"))
            .fromStatusId(UUID.fromString("66496069-bdac-451d-8509-7d0282c1f438"))
            .toStatusId(UUID.fromString("6f819e40-3d4a-4e27-9017-5a20e9dbb29a"))
            .fromEmployeeId(UUID.fromString("ca4b41a4-4416-4465-acdc-3bf340d2031c"))
            .toEmployeeId(UUID.fromString("b7b06e2d-d4f7-4cab-872a-3861288b5da1"))
            .comment("2. Verification / Qualification test")
            .build();
    var requestBody = objectMapper.writeValueAsString(request);
    var response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    var status = response.getResponse().getStatus();
    var contentAsString = response.getResponse().getContentAsString();
    var responseBody = objectMapper.readValue(contentAsString, TicketHistoryDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertNotNull(responseBody.getId());
    assertEquals("2025-T-23", responseBody.getTicketNumber());
    assertEquals("Monitor rocket systems", responseBody.getTicketTitle());
    assertEquals("Registration", responseBody.getFromStatusName());
    assertEquals("Verification / Qualification", responseBody.getToStatusName());
    assertEquals("Billie Braun", responseBody.getFromEmployeeName());
    assertEquals("Kiana Kertzmann", responseBody.getToEmployeeName());
    assertEquals("2. Verification / Qualification test", responseBody.getComment());
  }

  @Test
  void shouldReturn_400_createTicketHistory() throws Exception {
    var request =
        TicketHistoryCreateRequestDTO.builder()
            .ticketId(UUID.fromString("52f66d4e-79bf-4e85-93f9-405322fe7a4a"))
            .fromStatusId(UUID.fromString("66496069-bdac-451d-8509-7d0282c1f438"))
            .toStatusId(UUID.fromString("6f819e40-3d4a-4e27-9017-5a20e9dbb29a"))
            .fromEmployeeId(UUID.fromString("ca4b41a4-4416-4465-acdc-3bf340d2031c"))
            .toEmployeeId(null)
            .comment("2. Verification / Qualification test")
            .build();
    var requestBody = objectMapper.writeValueAsString(request);
    var response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    var status = response.getResponse().getStatus();
    var contentAsString = response.getResponse().getContentAsString();
    var responseBody = objectMapper.readValue(contentAsString, ReactAdminBadRequestError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals("must not be null", responseBody.getErrors().get("toEmployeeId"));
  }

  @Test
  void shouldReturn_401_createTicketHistory() throws Exception {
    var request =
        TicketHistoryCreateRequestDTO.builder()
            .ticketId(UUID.fromString("52f66d4e-79bf-4e85-93f9-405322fe7a4a"))
            .fromStatusId(UUID.fromString("66496069-bdac-451d-8509-7d0282c1f438"))
            .toStatusId(UUID.fromString("6f819e40-3d4a-4e27-9017-5a20e9dbb29a"))
            .fromEmployeeId(UUID.fromString("ca4b41a4-4416-4465-acdc-3bf340d2031c"))
            .toEmployeeId(UUID.fromString("b7b06e2d-d4f7-4cab-872a-3861288b5da1"))
            .comment("2. Verification / Qualification test")
            .build();
    var requestBody = objectMapper.writeValueAsString(request);
    var response =
        mockMvc
            .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    var status = response.getResponse().getStatus();
    var contentAsString = response.getResponse().getContentAsString();
    var responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_createTicketHistory() throws Exception {
    var request =
        TicketHistoryCreateRequestDTO.builder()
            .ticketId(UUID.fromString("52f66d4e-79bf-4e85-93f9-405322fe7a4a"))
            .fromStatusId(UUID.fromString("66496069-bdac-451d-8509-7d0282c1f438"))
            .toStatusId(UUID.fromString("6f819e40-3d4a-4e27-9017-5a20e9dbb29a"))
            .fromEmployeeId(UUID.fromString("ca4b41a4-4416-4465-acdc-3bf340d2031c"))
            .toEmployeeId(UUID.fromString("b7b06e2d-d4f7-4cab-872a-3861288b5da1"))
            .comment("2. Verification / Qualification test")
            .build();
    var requestBody = objectMapper.writeValueAsString(request);
    var response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withBadJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    var status = response.getResponse().getStatus();
    var contentAsString = response.getResponse().getContentAsString();
    var responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }
}
