package com.pawelnu.projectmanager.endpoints.category.value;

import static com.pawelnu.projectmanager.utils.Utils.FULL_AUTH_IS_REQUIRED;
import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
class CategoryValueControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_CATEGORY_VALUES;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password");

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @BeforeEach
  void beforeEach() {
    Utils.generateToken(jwtUtils);
  }

  @Test
  void shouldReturn_201_createCategoryValue() throws Exception {
    CategoryValueCreateRequestDTO request =
        CategoryValueCreateRequestDTO.builder()
            .categoryId(UUID.fromString("3f9e9b4b-bd4f-4112-93ac-d4804107838c"))
            .numericValue(BigDecimal.valueOf(1))
            .stringValue("1")
            .dateValue(LocalDateTime.of(2025, 6, 22, 11, 10, 0).toInstant(ZoneOffset.UTC))
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
    CategoryValueDTO responseBody = objectMapper.readValue(contentAsString, CategoryValueDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("category a", responseBody.getCategoryName());
    assertEquals(request.getNumericValue(), responseBody.getNumericValue());
    assertEquals(request.getStringValue(), responseBody.getStringValue());
    assertEquals(request.getStringValue(), responseBody.getStringValue());
  }

  @Test
  void shouldReturn_400_createCategoryValue() throws Exception {
    CategoryValueCreateRequestDTO request =
        CategoryValueCreateRequestDTO.builder()
            .categoryId(null)
            .numericValue(BigDecimal.valueOf(1))
            .stringValue("1")
            .dateValue(LocalDateTime.of(2025, 6, 22, 11, 10, 0).toInstant(ZoneOffset.UTC))
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
    assertEquals("must not be null", responseBody.getErrors().get("categoryId"));
  }

  @Test
  void shouldReturn_401_createCategoryValue() throws Exception {
    CategoryValueCreateRequestDTO request =
        CategoryValueCreateRequestDTO.builder()
            .categoryId(UUID.fromString("3f9e9b4b-bd4f-4112-93ac-d4804107838c"))
            .numericValue(BigDecimal.valueOf(1))
            .stringValue("1")
            .dateValue(LocalDateTime.of(2025, 6, 22, 11, 10, 0).toInstant(ZoneOffset.UTC))
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
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_createCategoryValue() throws Exception {
    CategoryValueCreateRequestDTO request =
        CategoryValueCreateRequestDTO.builder()
            .categoryId(UUID.fromString("3f9e9b4b-bd4f-4112-93ac-d4804107838c"))
            .numericValue(BigDecimal.valueOf(1))
            .stringValue("1")
            .dateValue(LocalDateTime.of(2025, 6, 22, 11, 10, 0).toInstant(ZoneOffset.UTC))
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

  //  TODO Test void getList()

  //  TODO Test void getById()

  //  TODO Test void editById()

  //  TODO Test void deleteById()
}
