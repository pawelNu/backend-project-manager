package com.pawelnu.projectmanager.endpoints.ticket;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
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
import org.springframework.test.web.servlet.MvcResult;
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
  private static final String BASE_URL = "/" + Path.API_PROJECT_STEP_COMMENTS;

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

  // TODO adjust tests
  @Test
  void shouldReturn_201_createTicket() throws Exception {
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .comment("comment for test")
            .projectStepId(UUID.fromString("9e9885ce-86d7-4ce7-8936-2db459fc6530"))
            .employeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    TicketDTO responseBody = objectMapper.readValue(contentAsString, TicketDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertNotNull(responseBody.getId());
    assertEquals(request.getComment(), responseBody.getComment());
    assertEquals(request.getProjectStepId(), responseBody.getStepId());
    assertEquals(
        UUID.fromString("7ebf87eb-be90-45d8-b92c-25701897211f"), responseBody.getProjectId());
    assertEquals(
        UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"), responseBody.getEmployeeId());
  }

  @Test
  void shouldReturn_400_createTicket() throws Exception {
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .comment("test")
            .projectStepId(UUID.fromString("9e9885ce-86d7-4ce7-8936-2db459fc6530"))
            .employeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminBadRequestError responseBody =
        objectMapper.readValue(contentAsString, ReactAdminBadRequestError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(
        "Comment has to be at least 5 characters", responseBody.getErrors().get("comment"));
  }

  @Test
  void shouldReturn_401_createTicket() throws Exception {
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .comment("comment for test")
            .projectStepId(UUID.fromString("9e9885ce-86d7-4ce7-8936-2db459fc6530"))
            .employeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_createTicket() throws Exception {
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .comment("comment for test")
            .projectStepId(UUID.fromString("9e9885ce-86d7-4ce7-8936-2db459fc6530"))
            .employeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withBadJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }
}
