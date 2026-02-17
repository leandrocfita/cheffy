package br.com.fiap.cheffy.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @Test
    void openApiSpecCreatesBean() {
        SwaggerConfig config = new SwaggerConfig();

        OpenAPI openAPI = config.openApiSpec();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getComponents()).isNotNull();
        assertThat(openAPI.getComponents().getSchemas()).containsKey("ApiErrorResponse");
        assertThat(openAPI.getComponents().getSchemas()).containsKey("ApiFieldError");
    }

    @Test
    void operationCustomizerCreatesBean() {
        SwaggerConfig config = new SwaggerConfig();

        OperationCustomizer customizer = config.operationCustomizer();

        assertThat(customizer).isNotNull();
    }
}
