package au.com.ibenta.template;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

@Api(tags = "template")
@RestController
@Profile("template")
@RequestMapping("/error")
public class TemplateErrorController {

    @GetMapping("/{code}")
    public void error(@PathVariable("code") final int code) {
        if (code == 400) {
            throw new TemplateException(Status.BAD_REQUEST);
        } else {
            throw new TemplateException(Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static class TemplateException extends AbstractThrowableProblem {

        TemplateException(final StatusType status) {
            super(null, status.getReasonPhrase(), status, status.getReasonPhrase());
        }
    }
}
