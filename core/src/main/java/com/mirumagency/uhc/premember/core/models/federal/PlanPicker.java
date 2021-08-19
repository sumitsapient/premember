package com.mirumagency.uhc.premember.core.models.federal;

import com.adobe.acs.commons.util.ModeUtil;
import com.day.cq.wcm.api.WCMMode;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = PlanPicker.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlanPicker {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String IMAGE_CHILD_RESOURCE_NAME = "image";
    private static final String BUTTON_CHILD_RESOURCE_NAME = "button";

    @ScriptVariable
    private Resource resource;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private List<PlanOptionDto> options = new ArrayList<>();

    @Getter
    private PlanOptionDto selectedOption;

    @Inject
    private PlansService plansService;

    @Getter
    private Boolean imageEmpty;

    @Getter
    private Boolean buttonEmpty;

    @SlingObject
    private SlingHttpServletResponse response;

    private String regionCode;

    private Plan plan;

    @PostConstruct
    protected void init() {
        String pageUrl = request.getPathInfo();
        String regionCookieName = getRegionCookieName(pageUrl);
        Cookie regionCodeCookie = request.getCookie(regionCookieName);
        if (regionCodeCookie != null) {
            regionCode = regionCodeCookie.getValue();
        }
        plansService.loadPlan(pageUrl, regionCode).ifPresent(this::mapToModel);
        imageEmpty = isPropertyEmpty(IMAGE_CHILD_RESOURCE_NAME, "fileReference");
        buttonEmpty = isPropertyEmpty(BUTTON_CHILD_RESOURCE_NAME, "href");

        if (shouldRedirectToSummary()) {
            redirectToSummaryPage();
        }
    }

    private String getRegionCookieName(String pageUrl) {
        PlanPath planType = new PlanPath(pageUrl);
        return planType.getPlanType() == PlanType.DENTAL ? "federal-dental-region-code" : "federal-region-code";
    }

    private Boolean isPropertyEmpty(String childComponentName, String propertyName) {
        return !Optional.ofNullable(resource.getChild(childComponentName))
                .map(Resource::getValueMap)
                .map(imageProperties -> imageProperties.get(propertyName, String.class))
                .filter(StringUtils::isNotBlank)
                .isPresent();
    }

    private void mapToModel(Plan plan) {
        this.plan = plan;
        if (plan.getSelectedOption() != null) {
            selectedOption = mapToOption(plan.getSelectedOption());
        }
        options = plan.getOptions().stream().map(this::mapToOption).collect(toList());
    }

    private PlanOptionDto mapToOption(PlanOption planOption) {
        return PlanOptionDto
                .builder()
                .id(planOption.getId())
                .name(planOption.getName())
                .featuredText(planOption.getFeaturedText())
                .description(planOption.getDetailsDescription())
                .build();
    }

    public boolean hasMultipleOptions() {
        return options.size() > 0;
    }

    private boolean shouldRedirectToSummary(){
        WCMMode mode = ModeUtil.getMode(request);
        boolean modeTriggersRedirection = mode.equals(WCMMode.DISABLED);
        return plan != null && !plan.isSelectedOptionAllowed() && modeTriggersRedirection;
    }

    private void redirectToSummaryPage() {
        try {
            logger.warn("Invalid selected [{}] plan, for region code [{}]. User will be redirected " +
                    "to the summary page", plan.getType().getTypeName(), regionCode);
            String summaryPageUrl = PlanPath.getPlanPage(plan.getType());
            response.sendRedirect(summaryPageUrl);
        } catch (IOException e) {
            logger.error("Redirection to summary page has failed", e);
        }
    }

    @Builder
    @Getter
    @Setter
    public static class PlanOptionDto {
        private String id;
        private String name;
        private String description;
        private String featuredText;
    }
}