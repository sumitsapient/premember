package com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.util.Asserts.notEmpty;

public class PlanPath {
    private static final String PLAN_DETAILS_PAGE_NAME_REGEXP = "/content/premember/.+/en/home/([a-z]+)" +
            "-plans/plan-details(\\.([^.]+)(\\.([^.]+))?)?";
    private static final String ANY_PLANS_PAGE_REGEX = "^/content/premember/employer.+/en/home/(\\w+)-plans(.)?";

    public static final String ROOT_PLAN_PATH = "/content/premember/employers/federal/en/home/%s-plans";

    @Getter
    private final String planOptionName;
    @Getter
    private final PlanType planType;
    @Getter
    private final String path;

    public PlanPath(String pageUrl) {
        notEmpty(pageUrl, "pageUrl");
        path = FilenameUtils.removeExtension(pageUrl);
        planType = extractPlanType();
        planOptionName = extractPlanOptionName();
    }

    private PlanType extractPlanType() {
        String planType =  extractElement(1, ANY_PLANS_PAGE_REGEX);
        return PlanType.from(planType).orElse(PlanType.NOT_SELECTED);
    }

    private String extractPlanOptionName() {
        return extractElement(3, PLAN_DETAILS_PAGE_NAME_REGEXP);
    }

    private String extractElement(int group, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return StringUtils.EMPTY;
    }

    public static String getPlanPage(PlanType planType){
        return String.format(ROOT_PLAN_PATH, planType.getTypeName());
    }
}
