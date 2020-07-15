package au.com.ibenta.problem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebExceptionHandler;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import static java.util.Optional.ofNullable;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;

@Configuration
public class ProblemConfiguration {

    @Bean
    ProblemModule problemModule(final ServerProperties serverProperties) {
        final ProblemModule module = new ProblemModule();
        return ofNullable(serverProperties.getError())
                .map(ErrorProperties::getIncludeStacktrace)
                .map(ALWAYS::equals)
                .map(module::withStackTraces)
                .orElse(module);
    }

    @Bean
    ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }

    @Bean
    @Order(-2)
    WebExceptionHandler problemExceptionHandler(final ObjectMapper mapper,
                                                final ProblemHandling problemHandling) {
        return new ProblemExceptionHandler(mapper, problemHandling);
    }
}
