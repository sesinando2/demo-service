package au.com.ibenta.template;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.lang.String.format;

@ActiveProfiles(value = {"template", "test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(ids = "au.com.ibenta:springboot-template:+:stubs")
public abstract class BaseTestConsumerClass {

    @StubRunnerPort("springboot-template")
    private Integer springBootTemplatePort;

    protected WebTestClient springbootTemplateClient;

    @BeforeEach
    public void setup() {
        springbootTemplateClient = WebTestClient.bindToServer()
                .baseUrl(format("http://localhost:%d", springBootTemplatePort))
                .build();
    }
}
