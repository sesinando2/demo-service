package au.com.ibenta.template;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

import static java.lang.String.format;

@Api(tags = "template")
@RestController
@Profile("template")
@RequiredArgsConstructor
@RequestMapping("/template")
public class TemplateController {

    private final BuildProperties buildProperties;

    @GetMapping("/version")
    Mono<ResponseEntity<String>> version() {
        return Mono.justOrEmpty(buildProperties).map(BuildProperties::getVersion).map(ResponseEntity::ok);
    }

    @GetMapping
    Flux<Template> list() {
        return Flux.fromStream(IntStream.range(1, 11).boxed())
                .map(String::valueOf)
                .map(id -> Template.builder().id(id).name(format("name is %s", id)).build());
    }

    @GetMapping("/{id}")
    ResponseEntity<Mono<Template>> get(@PathVariable("id") final String id) {
        return ResponseEntity.ok(Mono.just(Template.builder().id(id).name(format("name: %s", id)).build()));
    }
}
