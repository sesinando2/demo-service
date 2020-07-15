package au.com.ibenta.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@DisplayName("test template endpoints")
@AutoConfigureWebTestClient
public class TemplateControllerTests extends BaseTestClass {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("test get one template")
    public void testGetTemplate() {
        webTestClient.get().uri("/template/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(1);
    }

    @Test
    @DisplayName("test list all templates")
    public void testListTemplates() {
        webTestClient.get().uri("/template")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(10)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[2].id").isEqualTo(3)
                .jsonPath("$[3].id").isEqualTo(4)
                .jsonPath("$[4].id").isEqualTo(5)
                .jsonPath("$[5].id").isEqualTo(6)
                .jsonPath("$[6].id").isEqualTo(7)
                .jsonPath("$[7].id").isEqualTo(8)
                .jsonPath("$[8].id").isEqualTo(9)
                .jsonPath("$[9].id").isEqualTo(10);
    }

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {"BAD_REQUEST", "INTERNAL_SERVER_ERROR"})
    @DisplayName("test errors")
    public void testError(final HttpStatus error) {
        webTestClient.get().uri("/error/{code}", error.value())
                .exchange().expectStatus().isEqualTo(error)
                .expectBody()
                .jsonPath("$.title").isEqualTo(error.getReasonPhrase())
                .jsonPath("$.detail").isEqualTo(error.getReasonPhrase());
    }
}
