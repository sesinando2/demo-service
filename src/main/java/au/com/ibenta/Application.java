package au.com.ibenta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.InetAddress;
import java.net.URI;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

@Slf4j
@SpringBootApplication
public class Application {

    private final Environment environment;
    private final GitProperties gitProperties;
    private final BuildProperties buildProperties;

    public Application(final Environment environment,
                       @Autowired(required = false)
                       final GitProperties gitProperties,
                       @Autowired(required = false)
                       final BuildProperties buildProperties) {

        this.environment = environment;
        this.gitProperties = gitProperties;
        this.buildProperties = buildProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> index() {

        HandlerFunction<ServerResponse> redirectToSwagger = req ->
                permanentRedirect(URI.create("/swagger-ui.html")).build();

        return route(GET(""), redirectToSwagger)
                .andRoute(GET("/"), redirectToSwagger)
                .andRoute(GET("/index"), redirectToSwagger)
                .andRoute(GET("/index.html"), redirectToSwagger);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        final var format = "%n"
                + "+-------------------------------------------------------------------------------------%n"
                + "|   Application %2$s v%8$s (%9$s) is running! Access URLs:%n"
                + "|   Local               : %1$s://localhost:%3$s%5$s%n"
                + "|   ApiDoc              : %1$s://localhost:%3$s%5$s/swagger-ui.html %n"
                + "|   Management          : %1$s://localhost:%4$s%5$s/actuator%n"
                + "|   Available Resources : %1$s://localhost:%4$s%5$s/actuator/mappings%n"
                + "|   External            : %1$s://%6$s:%4$s%5$s%n"
                + "|   Profile(s)          : %7$s%n"
                + "+-------------------------------------------------------------------------------------";

        final var appInfo = String.format(
                format,
                ofNullable(environment.getProperty("server.ssl.key-store")).map(store -> "https").orElse("http"),
                ofNullable(buildProperties).map(BuildProperties::getName).orElse("Unknown Build"),
                environment.getProperty("server.port"),
                ofNullable(environment.getProperty("management.server.port")).orElse(environment.getProperty("server.port")),
                ofNullable(environment.getProperty("server.servlet.context-path")).orElse(""),
                getHostAddress(),
                stream(environment.getActiveProfiles()).collect(Collectors.joining(", ")),
                ofNullable(buildProperties).map(BuildProperties::getVersion).orElse("Unknown Version"),
                ofNullable(gitProperties).map(GitProperties::getShortCommitId).orElse("Unknown Commit")
        );

        log.info(appInfo);
    }

    private String getHostAddress() {
        String hostAddress;

        try {
            hostAddress = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Unable to get host address", e);
            hostAddress = "localhost";
        }

        return hostAddress;
    }
}
