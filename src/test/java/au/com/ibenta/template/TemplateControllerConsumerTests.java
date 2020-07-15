package au.com.ibenta.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TemplateControllerConsumerTests extends BaseTestConsumerClass {

    @Test
    @DisplayName("test get one template")
    public void testGetTemplate() {
        springbootTemplateClient.get().uri("/template/{id}", 1001)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(1001);
    }
}
