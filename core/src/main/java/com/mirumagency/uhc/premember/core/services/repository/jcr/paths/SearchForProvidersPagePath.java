package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.EmployerPlansException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.util.Asserts.check;
import static org.apache.http.util.Asserts.notNull;

public class SearchForProvidersPagePath extends Path {

    public static final String SEARCH_FOR_PROVIDERS_PAGE_NAME_REGEXP = "(/content/premember/.+/en/home/search-for-a-provider).*";
    private static final Pattern searchForProvidersPagePathPattern = Pattern.compile(SEARCH_FOR_PROVIDERS_PAGE_NAME_REGEXP);

    private final String path;

    public static SearchForProvidersPagePath of(String resourcePath) {
        notNull(resourcePath, "resourcePath");
        return new SearchForProvidersPagePath(resourcePath);
    }

    private SearchForProvidersPagePath(String resourcePath) {
        notNull(resourcePath, "resourcePath");
        check(isSearchForProvidersPagePath(resourcePath), "Resource path must be a plan page path!");
        this.path = calculateRoot(resourcePath);
    }

    public static boolean isSearchForProvidersPagePath(String resourcePath) {
        Matcher matcher = searchForProvidersPagePathPattern.matcher(resourcePath);
        return matcher.find();
    }

    private String calculateRoot(String resourcePath) {
        Matcher matcher = searchForProvidersPagePathPattern.matcher(resourcePath);
        if (matcher.find()) {
            return matcher.group(1);
        }
       throw new EmployerPlansException(String.format("Can't resolve search for prescription page path from: %s", resourcePath));
    }

    public PlanType extractPlanType() {
        return PlanType.HEALTH;
    }

    @Override
    public String path() {
        return path;
    }
}
