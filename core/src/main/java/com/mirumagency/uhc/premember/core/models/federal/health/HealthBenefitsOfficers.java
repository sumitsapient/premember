package com.mirumagency.uhc.premember.core.models.federal.health;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.HealthBenefitsOfficer;
import com.mirumagency.uhc.premember.core.services.federal.HealthBenefitsOfficerService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = HealthBenefitsOfficers.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HealthBenefitsOfficers {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    HealthBenefitsOfficerService healthBenefitsOfficerService;

    @Getter
    private String officersAsJson;

    private List<HealthBenefitsOfficer> healthBenefitsOfficers = new ArrayList<>();

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {
        Cookie regionCodeCookie = request.getCookie("federal-region-code");
        if (regionCodeCookie != null) {
            String regionCode = regionCodeCookie.getValue();
            healthBenefitsOfficers = healthBenefitsOfficerService.loadOfficers(regionCode);
        }
        try {
            officersAsJson = JsonUtil.mapToJson(healthBenefitsOfficers, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            officersAsJson = StringUtils.EMPTY;
        }
    }

    public boolean isEmpty(){
        return healthBenefitsOfficers.isEmpty() || StringUtils.isEmpty(officersAsJson);
    }
}
