package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.check;
import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.EmployerPlansException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchForPrescriptionPagePath extends Path {

    public static final String SEARCH_FOR_PRESCRIPTION_PAGE_NAME_REGEXP = "(/content/premember/.+/en/home/search-for-a-prescription).*";
    private static final Pattern searchForPrescriptionPagePathPattern = Pattern.compile(SEARCH_FOR_PRESCRIPTION_PAGE_NAME_REGEXP);

    private final String path;

    public static SearchForPrescriptionPagePath of(String resourcePath) {
        notNull(resourcePath, "resourcePath");
        return new SearchForPrescriptionPagePath(resourcePath);
    }

    private SearchForPrescriptionPagePath(String resourcePath) {
        notNull(resourcePath, "resourcePath");
        check(isSearchForPrescriptionPagePath(resourcePath), "Resource path must be a plan page path!");
        this.path = calculateRoot(resourcePath);
    }

    public static boolean isSearchForPrescriptionPagePath(String resourcePath) {
        Matcher matcher = searchForPrescriptionPagePathPattern.matcher(resourcePath);
        return matcher.find();
    }

    private String calculateRoot(String resourcePath) {
        Matcher matcher = searchForPrescriptionPagePathPattern.matcher(resourcePath);
        if (matcher.find()) {
            return matcher.group(1);
        }
       throw new EmployerPlansException(String.format("Can't resolve search for prescription page path from: %s", resourcePath));
    }

    public PlanType extractPlanType() {
        return PlanType.PHARMACY;
    }

    @Override
    public String path() {
        return path;
    }
}
