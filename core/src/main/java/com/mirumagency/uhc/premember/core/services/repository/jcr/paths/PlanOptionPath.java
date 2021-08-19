package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.EmployerPlansException;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class PlanOptionPath extends Path {

    private static final String FRAGMENT_PATH_PROP = "fragmentPath";
    private static final String VARIATION_NAME_PROP = "variationName";

    private static final String CONTENT_FRAGMENT_RESOURCE_PATH_TEMPLATE = "%s/jcr:content/data/%s";
    private static final String PLAN_OPTION_URL_REGEXP = "(/content/dam/premember/plans/options/(.+)/(.+)/jcr:content/data/(.+))";
    private static final Pattern planOptionUrlPattern = Pattern.compile(PLAN_OPTION_URL_REGEXP);

    private final String path;

    public PlanOptionPath(String planOptionResourcePath) {
        notNull(planOptionResourcePath, "planOptionResourcePath");
        path = planOptionResourcePath;
    }

    public static Optional<PlanOptionPath> from(NiceResource planConfigResource) {
        return Optional.of(planConfigResource)
            .filter(PlanOptionPath::isContentFragmentAdded)
            .map(PlanOptionPath::resolvePlanPathFromPlanConfig);
    }

    private static boolean isContentFragmentAdded(NiceResource planConfig) {
        return !(StringUtils.isBlank(planConfig.getString(FRAGMENT_PATH_PROP))
            || StringUtils.isBlank(planConfig.getString(VARIATION_NAME_PROP)));
    }

    private static PlanOptionPath resolvePlanPathFromPlanConfig(NiceResource planConfigResource) {
        String fragmentPath = planConfigResource.getString(FRAGMENT_PATH_PROP);
        String variationName = planConfigResource.getString(VARIATION_NAME_PROP);
        String planOptionResourcePath = String
            .format(CONTENT_FRAGMENT_RESOURCE_PATH_TEMPLATE, fragmentPath, variationName);
        return new PlanOptionPath(planOptionResourcePath);
    }

    public PlanType extractPlanType() {
        Matcher matcher = planOptionUrlPattern.matcher(path);
        if (matcher.find()) {
            return PlanType.from(matcher.group(2)).orElseThrow(IllegalArgumentException::new);
        } else {
            throw new EmployerPlansException("Can't extract plan type from resource path:" + path);
        }
    }

    public String extractPlanName() {
        Matcher matcher = planOptionUrlPattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(3);
        } else {
            throw new EmployerPlansException("Can't extract plan name from resource path:" + path);
        }
    }

    public String extractPlanVariation() {
        Matcher matcher = planOptionUrlPattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(4);
        } else {
            throw new EmployerPlansException("Can't extract plan variation from resource path:" + path);
        }
    }

    @Override
    public String path() {
        return path;
    }
}
