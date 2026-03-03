package com.pawelnu.projectmanager.endpoints.ticket;

import static com.pawelnu.projectmanager.utils.Utils.invalidUUIDError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.BaseIntegrationTest;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
class TicketControllerDeleteByIdTest extends BaseIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_TICKETS;

  @Test
  void shouldReturn_200_deleteTicketById_isDeletedFalse() throws Exception {
    String ticketId = "13531048-a6c6-492a-a84f-8ec3c955e93d";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Deleted ticket with id: " + ticketId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteTicketById_isDeletedFalse() throws Exception {
    String ticketId = "invalid-uuid";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_deleteTicketById_isDeletedFalse() throws Exception {
    String ticketId = "13531048-a6c6-492a-a84f-8ec3c955e93d";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_deleteTicketById_isDeletedFalse() throws Exception {
    String ticketId = "13531048-a6c6-492a-a84f-8ec3c955e93d";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withBadJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_deleteTicketById_isDeletedFalse() throws Exception {
    String ticketId = "81b9f13a-d598-4a24-bdef-833e02e30efd";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.TICKET_NOT_FOUND + ticketId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteTicketById_isDeletedTrue() throws Exception {
    String ticketId = "610b92c2-8a12-47c2-977f-30f19769f265";
    String url = BASE_URL + "/" + ticketId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.TICKET_NOT_FOUND + ticketId, responseBody.getMessage());
  }
}
