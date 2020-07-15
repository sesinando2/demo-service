package au.com.ibenta.springfox;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.schema.AlternateTypeRule;

import java.util.ArrayList;
import java.util.List;

public class RecursiveAlternateTypeRule extends AlternateTypeRule {

    private final TypeResolver resolver;
    private final List<AlternateTypeRule> rules;

    public RecursiveAlternateTypeRule(final TypeResolver resolver,
                                      final List<AlternateTypeRule> rules) {
        super(resolver.resolve(Object.class), resolver.resolve(Object.class));
        this.resolver = resolver;
        this.rules = new ArrayList<>(rules);
    }

    @Override
    public ResolvedType alternateFor(final ResolvedType type) {
        final var newType = rules.stream()
                .map(rule -> rule.alternateFor(type))
                .filter(resolvedType -> !resolvedType.equals(type))
                .findFirst()
                .orElse(type);

        return appliesTo(newType) ? alternateFor(newType) : newType;
    }

    @Override
    public boolean appliesTo(final ResolvedType type) {
        return rules.stream().anyMatch(rule -> rule.appliesTo(type));
    }
}
