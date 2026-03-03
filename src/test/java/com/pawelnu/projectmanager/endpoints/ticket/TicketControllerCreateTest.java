package com.pawelnu.projectmanager.endpoints.ticket;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.BaseIntegrationTest;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
class TicketControllerCreateTest extends BaseIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_TICKETS;

  @Test
  void shouldReturn_201_createTicket() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 8, 30, 0, 0).toInstant(ZoneOffset.UTC);
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .title("test ticket for testing")
            .categoryValueId(UUID.fromString("9210ed20-3f01-4c0a-947a-67f93caf557e"))
            .deadline(deadline)
            .priorityValueId(UUID.fromString("31259c88-a717-454c-bdd0-1dfa19245b77"))
            .additionalDetails("additional details for testing")
            .projectId(UUID.fromString("6e723e03-4a67-475c-b614-d3993947e596"))
            .projectStepId(UUID.fromString("736cc0e9-b71f-4913-8974-ec6f0e942ae3"))
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
    assertEquals(request.getTitle(), responseBody.getTitle());
    assertEquals(request.getDeadline(), responseBody.getDeadline());
    assertEquals(request.getAdditionalDetails(), responseBody.getAdditionalDetails());
    assertEquals(request.getCategoryValueId(), responseBody.getCategoryValueId());
    assertEquals(request.getPriorityValueId(), responseBody.getPriorityValueId());
    assertEquals(request.getProjectId(), responseBody.getProjectId());
    assertEquals(request.getProjectStepId(), responseBody.getProjectStepId());
  }

  @Test
  void shouldReturn_400_createTicket() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 8, 30, 0, 0).toInstant(ZoneOffset.UTC);
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .title("test")
            .categoryValueId(UUID.fromString("9210ed20-3f01-4c0a-947a-67f93caf557e"))
            .deadline(deadline)
            .priorityValueId(UUID.fromString("31259c88-a717-454c-bdd0-1dfa19245b77"))
            .additionalDetails("additional details for testing")
            .projectId(UUID.fromString("6e723e03-4a67-475c-b614-d3993947e596"))
            .projectStepId(UUID.fromString("736cc0e9-b71f-4913-8974-ec6f0e942ae3"))
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
    assertEquals("Title must be at least 5 characters", responseBody.getErrors().get("title"));
  }

  @Test
  void shouldReturn_401_createTicket() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 8, 30, 0, 0).toInstant(ZoneOffset.UTC);
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .title("test ticket for testing")
            .categoryValueId(UUID.fromString("9210ed20-3f01-4c0a-947a-67f93caf557e"))
            .deadline(deadline)
            .priorityValueId(UUID.fromString("31259c88-a717-454c-bdd0-1dfa19245b77"))
            .additionalDetails("additional details for testing")
            .projectId(UUID.fromString("6e723e03-4a67-475c-b614-d3993947e596"))
            .projectStepId(UUID.fromString("736cc0e9-b71f-4913-8974-ec6f0e942ae3"))
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
    Instant deadline = LocalDateTime.of(2025, 8, 30, 0, 0).toInstant(ZoneOffset.UTC);
    TicketCreateRequestDTO request =
        TicketCreateRequestDTO.builder()
            .title("test ticket for testing")
            .categoryValueId(UUID.fromString("9210ed20-3f01-4c0a-947a-67f93caf557e"))
            .deadline(deadline)
            .priorityValueId(UUID.fromString("31259c88-a717-454c-bdd0-1dfa19245b77"))
            .additionalDetails("additional details for testing")
            .projectId(UUID.fromString("6e723e03-4a67-475c-b614-d3993947e596"))
            .projectStepId(UUID.fromString("736cc0e9-b71f-4913-8974-ec6f0e942ae3"))
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
