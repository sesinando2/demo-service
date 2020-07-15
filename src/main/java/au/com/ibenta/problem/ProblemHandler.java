package au.com.ibenta.problem;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.general.GeneralAdviceTrait;
import org.zalando.problem.spring.webflux.advice.http.HttpAdviceTrait;
import org.zalando.problem.spring.webflux.advice.network.NetworkAdviceTrait;
import org.zalando.problem.spring.webflux.advice.validation.ValidationAdviceTrait;

@ControllerAdvice
public class ProblemHandler implements ProblemHandling,
        GeneralAdviceTrait, HttpAdviceTrait, NetworkAdviceTrait, ValidationAdviceTrait {
}
