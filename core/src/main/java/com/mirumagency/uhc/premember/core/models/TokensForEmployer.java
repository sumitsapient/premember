package com.mirumagency.uhc.premember.core.models;

import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.isPlanResourcePath;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.SearchForProvidersPagePath.isSearchForProvidersPagePath;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.EmployerService;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.*;
import com.mirumagency.uhc.premember.core.util.RequestCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = TokensForEmployer.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TokensForEmployer extends Tokens {

    private transient ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private Page currentPage;

    @Inject
    private EmployerService employerService;

    @Self
    private SlingHttpServletRequest request;

    @Override
    public Map<String, String> getTokens() {
        boolean includePlanTokens = isNotFederalSite();
        Map<String, String> tokens = new HashMap<>();
        if(includePlanTokens){
            tokens.putAll(planTokens());
        }
        tokens.putAll(employerTokens(includePlanTokens));
        return ImmutableMap.<String, String>builder()
                .putAll(tokens)
                .build();
    }

    public String getCrossPlanProvidersAsJson() {
        return mapToJson(getEmployer().getCrossPlanSearch());
    }

    protected String mapToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", object, e);
            return StringUtils.EMPTY;
        }
    }

    private Map<String, String> planTokens() {
        boolean isPlanPagePath = resourcePath()
                .map(this::isPlanPage)
                .orElse(false);
        if (isPlanPagePath) {
            return plan().getTokens();
        }
        return ImmutableMap.of();
    }

    protected Plan plan() {
        return requestCache()
                .getPlan(this::loadPlan);
    }

    private Plan loadPlan() {
        PlanType planType = currentPlanType();
        return getEmployer().getPlans().getPlanByType(planType)
                .withSelectedOptionId(planOptionId());
    }

    private PlanType currentPlanType() {
        return resourcePath()
                .map(this::calculatePlanTypeBasedOn)
                .orElse(PlanType.NOT_SELECTED);
    }

    protected PlanType calculatePlanTypeBasedOn(String resourcePath){
        if(isPlanResourcePath(resourcePath)) {
            return PlanPagePath.of(resourcePath).getPlanType();
        }
        if(isSearchForPrescriptionPagePath(resourcePath)){
            return SearchForPrescriptionPagePath.of(resourcePath).extractPlanType();
        }
        if(isSearchForProvidersPagePath(resourcePath)){
          return SearchForProvidersPagePath.of(resourcePath).extractPlanType();
        }
        return PlanType.NOT_SELECTED;
    }

    private boolean isPlanPage(String resourcePath){
        return isPlanResourcePath(resourcePath) || isSearchForPrescriptionPagePath(resourcePath) || isSearchForProvidersPagePath(resourcePath);
    }

    private String planOptionId() {
        boolean isPlanDetailsPagePath = resourcePath()
                .map(PlanDetailsPagePath::isPlanDetailsPagePath)
                .orElse(false);
        if (isPlanDetailsPagePath) {
            return planDetailsPagePath().planOptionId();
        }
        return "";
    }

    private PlanDetailsPagePath planDetailsPagePath() {
        return new PlanDetailsPagePath(request.getPathInfo());
    }

    protected Map<String, String> employerTokens(boolean includePlanTokens) {
        return getEmployer().getTokens(includePlanTokens);
    }

    public Employer getEmployer() {
        return requestCache()
                .getEmployer(this::loadEmployer);
    }

    private Employer loadEmployer() {
        boolean includePlans = isNotFederalSite();
        return resourcePath()
                .map(path -> employerService.loadEmployer(path, includePlans))
                .orElseGet(() -> employerService.loadEmployer("", false));
    }

    private boolean isNotFederalSite(){
        String resourcePath = resourcePath().orElseThrow(() -> new IllegalArgumentException("No resource path provided"));
        return !employerService.isFederalSite(resourcePath);
    }

    private RequestCache requestCache() {
        return RequestCache.of(request);
    }

    protected Optional<String> resourcePath() {
        return Optional.ofNullable(currentPage)
                .map(Page::getPath);
    }

    boolean isBlueprint() {
        return resourcePath()
                .map(EmployerPath::of)
                .map(EmployerPath::isBlueprint)
                .orElse(false);
    }
}
