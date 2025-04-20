package com.pawelnu.projectmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
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

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("API").pathsToMatch("/**").build();
  }

  @Bean
  public OpenAPI openAPI() {
    StringBuilder description = new StringBuilder();
    description.append("Application: ").append("<b>").append("Project Manger").append("</b>");
    description.append("<br>");
    description.append("Database: ").append("<b>").append(databaseSID).append("</b>");
    String securitySchemeName = "Authorization";
    String protocol = "http";
    if (ssl) {
      protocol += "s";
    }
    return new OpenAPI()
        .info(
            new Info()
                .title("API Project Manger")
                .description(description.toString())
                .version(PomProperties.getReleaseWithDesc()))
        .externalDocs(
            new ExternalDocumentation()
                .url("/project-manager.yaml")
                .description("Download YAML docs"))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(
                            new OAuthFlows()
                                .clientCredentials(
                                    new OAuthFlow()
                                        .tokenUrl(
                                            protocol
                                                + "://"
                                                + hostname
                                                + ":"
                                                + port
                                                + "/oauth/token")))));
  }
}
