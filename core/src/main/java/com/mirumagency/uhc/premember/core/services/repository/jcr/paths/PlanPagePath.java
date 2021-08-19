package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.http.util.Asserts.check;
import static org.apache.http.util.Asserts.notNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.EmployerPlansException;

public class PlanPagePath extends Path {

    public static final String PLAN_PAGE_REGEXP = "(/content/premember/.+/en/home/(.+)-plans).*";
    private static final String PLAN_PAGE_HTML_REGEXP = "(/content/premember/.+/en/home/(.+)-plans)(.*)?\\.html.*";
    private static final Pattern planPagePathPattern = Pattern.compile(PLAN_PAGE_REGEXP);
    private static final String PLAN_PAGE_PATH_FORMAT = "%s/en/home/%s-plans";
    private static final String PLAN_COMPARISON_SUFFIX = "/comparison-table";
    private final String path;
    private final PlanType planType;

    public static PlanPagePath of(String resourcePath) {
        notNull(resourcePath, "resourcePath");
        check(isPlanResourcePath(resourcePath), "Resource path must be a plan page path!");
        String path = calculateRoot(resourcePath);
        PlanType planType = extractPlanType(path);
        return new PlanPagePath(path, planType);
    }

    private PlanPagePath(String path, PlanType planType) {
        notNull(path, "path");
        notNull(planType, "planType");
        this.path = path;
        this.planType = planType;
    }

    public static boolean isPlanResourcePath(String resourcePath) {
        Pattern pattern = Pattern.compile(PLAN_PAGE_REGEXP);
        Matcher matcher = pattern.matcher(resourcePath);
        return matcher.find();
    }

    public static boolean isPlanSummaryPagePath(String pagePath) {
        Pattern pattern = Pattern.compile(PLAN_PAGE_HTML_REGEXP);
        Matcher matcher = pattern.matcher(pagePath);
        return matcher.find() && isNullOrEmpty(matcher.group(3));
    }

    public static boolean isPlanComparisonPath(String pagePath) {
        Pattern pattern = Pattern.compile(PLAN_PAGE_HTML_REGEXP);
        Matcher matcher = pattern.matcher(pagePath);
        return matcher.find() && PLAN_COMPARISON_SUFFIX.equals(matcher.group(3));
    }


    private static String calculateRoot(String resourcePath) {
        Pattern pattern = Pattern.compile(PLAN_PAGE_REGEXP);
        Matcher matcher = pattern.matcher(resourcePath);
        if (matcher.find()) {
            return matcher.group(1);
        }
       throw new EmployerPlansException(String.format("Can't resolve plan page path from: %s", resourcePath));
    }

    private static PlanType extractPlanType(String path) {
        Matcher matcher = planPagePathPattern.matcher(path);
        if (matcher.find()) {
            return PlanType.from(matcher.group(2)).orElseThrow(IllegalArgumentException::new);
        } else {
            throw new EmployerPlansException("Can't extract plan type from resource path:" + path);
        }
    }

    @Override
    public String path() {
        return path;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public static String getPlanPage(EmployerPath employerPath, PlanType planType) {
        notNull(planType, "planType");
        notNull(employerPath, "employerPath");
        return getPlanPageForEmployer(employerPath.path(), planType);
    }

    private static String getPlanPageForEmployer(String employerPath, PlanType planType) {
        return String.format(PLAN_PAGE_PATH_FORMAT, employerPath, planType.getTypeName());
    }
}
