package com.pawelnu.projectmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

  @Value("${spring.datasource.url}")
  private String databaseSID;

  @Value("${server.port}")
  private String port;

  @Value("${api.host}")
  private String hostname;

  @Value("${server.ssl.enabled:false}")
  private Boolean ssl;

  private static final String SECURITY_SCHEME_NAME = "Authorization";

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("API").pathsToMatch("/**").build();
  }

  @Bean
  public OpenAPI openAPI() {
    String description =
        "Application: "
            + "<b>"
            + "Project Manger"
            + "</b>"
            + "<br>"
            + "Database: "
            + "<b>"
            + databaseSID
            + "</b>";
    String protocol = "http";
    if (ssl) {
      protocol += "s";
    }
    return new OpenAPI()
        .info(
            new Info()
                .title("API Project Manger")
                .description(description)
                .version(PomProperties.getReleaseWithDesc()))
        .externalDocs(
            new ExternalDocumentation()
                .url("/project-manager.yaml")
                .description("Download YAML docs"))
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .components(
            new Components()
                .addSecuritySchemes(
                    SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)));
  }
}
