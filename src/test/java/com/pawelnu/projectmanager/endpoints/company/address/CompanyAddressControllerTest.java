package com.pawelnu.projectmanager.endpoints.company.address;

import static com.pawelnu.projectmanager.utils.Utils.FULL_AUTH_IS_REQUIRED;
import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
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
class CompanyAddressControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_COMPANY_ADDRESSES;

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
  void shouldReturn_201_createCompanyAddress() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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
    CompanyAddressDTO responseBody =
        objectMapper.readValue(contentAsString, CompanyAddressDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("Pouros Group", responseBody.getCompanyName());
    assertEquals(request.getStreet(), responseBody.getStreet());
    assertEquals(request.getStreetNumber(), responseBody.getStreetNumber());
    assertEquals(request.getCity(), responseBody.getCity());
    assertEquals(request.getZipCode(), responseBody.getZipCode());
    assertEquals(request.getCountry(), responseBody.getCountry());
    assertEquals(request.getPhoneNumber(), responseBody.getPhoneNumber());
    assertEquals(request.getEmailAddress(), responseBody.getEmailAddress());
    assertEquals(request.getAddressType(), responseBody.getAddressType());
  }

  @Test
  void shouldReturn_400_createCompanyAddress() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("invalid.email")
            .addressType("K")
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
        "must be a well-formed email address", responseBody.getErrors().get("emailAddress"));
  }

  @Test
  void shouldReturn_401_createCompanyAddress() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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
  void shouldReturn_403_createCompanyAddress() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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

  @Test
  void shouldReturn_200_getCompanyAddressList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanyAddressDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
  }

  @Test
  void shouldReturn_200_getCompanyAddressList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("companyName", "hayes", "city", "west");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanyAddressDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/1", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("Hayes-Welch", responseBody.getFirst().getCompanyName());
    assertEquals("West Aleasestad", responseBody.getFirst().getCity());
  }

  @Test
  void shouldReturn_200_getCompanyAddressList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of("city", "DESC");
    Map<String, String> filter = Map.of("companyName", "hayes");
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
    List<CompanyAddressDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-2/3", headerContentRange);
    assertEquals(3, responseBody.size());
    assertEquals("West Aleasestad", responseBody.getFirst().getCity());
  }

  @Test
  void shouldReturn_200_getCompanyAddressList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    List<String> sort = List.of("city", "ASC");
    String rangeString = objectMapper.writeValueAsString(range);
    String sortString = objectMapper.writeValueAsString(sort);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL).with(withJwt()).param("sort", sortString).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanyAddressDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("Adrianfort", responseBody.getFirst().getCity());
  }

  @Test
  void shouldReturn_200_getCompanyAddressList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("city", "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanyAddressDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getCompanyAddressList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    ReactAdminError expectedResponse =
        new ReactAdminError("Full authentication is required to access this resource");
    assertEquals(expectedResponse, responseBody);
  }

  @Test
  void shouldReturn_403_getCompanyAddressList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getCompanyAddressById() throws Exception {
    String companyAddressId = "e3d2061e-ef6b-4f94-9442-43bdf4f1f6c7";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    CompanyAddressDTO responseBody =
        objectMapper.readValue(contentAsString, CompanyAddressDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(companyAddressId), responseBody.getId());
    assertEquals("Hayes-Welch", responseBody.getCompanyName());
    assertEquals("Huey Rapid", responseBody.getStreet());
    assertEquals("55", responseBody.getStreetNumber());
    assertEquals("West Aleasestad", responseBody.getCity());
    assertEquals("20624", responseBody.getZipCode());
    assertEquals("Kenya", responseBody.getCountry());
    assertEquals("251.126.8153", responseBody.getPhoneNumber());
    assertEquals("hayes_welch@example.com", responseBody.getEmailAddress());
    assertEquals("main", responseBody.getAddressType());
  }

  @Test
  void shouldReturn_400_getCompanyAddressById() throws Exception {
    String companyAddressId = "invalid-uuid";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(MSG.INVALID_UUID, responseBody.getMessage());
  }

  @Test
  void shouldReturn_401_getCompanyAddressById() throws Exception {
    String companyAddressId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_getCompanyAddressById() throws Exception {
    String companyAddressId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getCompanyAddressById() throws Exception {
    String companyAddressId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + companyAddressId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getCompanyAddressById_isDeletedTrue() throws Exception {
    String companyAddressId = "acf847a2-1b20-4a31-8872-93fcbbe7934f";
    String url = BASE_URL + "/" + companyAddressId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + companyAddressId, responseBody.getMessage());
  }

  // TODO Test editById() {}

  // TODO Test deleteById() {}
}
