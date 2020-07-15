package au.com.ibenta.springfox;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableOpenApi
public class SpringFoxConfiguration {

    private final TypeResolver resolver;
    private final BuildProperties buildProperties;

    public SpringFoxConfiguration(final TypeResolver resolver,
                                  @Autowired(required = false) final BuildProperties buildProperties) {
        this.resolver = resolver;
        this.buildProperties = buildProperties;
    }

    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .alternateTypeRules(monoTypeRule())
                .alternateTypeRules(fluxTypeRule());
    }

    private RecursiveAlternateTypeRule monoTypeRule() {
        return new RecursiveAlternateTypeRule(resolver, asList(

                newRule(resolver.resolve(Mono.class, WildcardType.class),
                        resolver.resolve(WildcardType.class)),

                newRule(resolver.resolve(ResponseEntity.class, WildcardType.class),
                        resolver.resolve(WildcardType.class))
        ));
    }

    private RecursiveAlternateTypeRule fluxTypeRule() {
        return new RecursiveAlternateTypeRule(resolver, asList(

                newRule(resolver.resolve(Flux.class, WildcardType.class),
                        resolver.resolve(List.class, WildcardType.class)),

                newRule(resolver.resolve(ResponseEntity.class, WildcardType.class),
                        resolver.resolve(WildcardType.class))
        ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(ofNullable(buildProperties).map(BuildProperties::getName).orElse("Unknown Build"),
                null,
                ofNullable(buildProperties).map(BuildProperties::getVersion).orElse("Unknown Version"),
                null,
                ApiInfo.DEFAULT_CONTACT,
                null,
                null,
                Collections.emptyList());
    }
}
