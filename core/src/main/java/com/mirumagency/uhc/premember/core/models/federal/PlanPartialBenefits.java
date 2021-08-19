package com.mirumagency.uhc.premember.core.models.federal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Model(adaptables = SlingHttpServletRequest.class,
		adapters = PlanPartialBenefits.class,
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlanPartialBenefits {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@SlingObject
	private SlingHttpServletRequest request;

	@Inject
	private PlansService plansService;

	@ValueMapValue @Default(values= EMPTY)
	private String benefitKey;

	@ValueMapValue @Default(booleanValues = false)
	private Boolean containsButton;

	@ValueMapValue @Default(values= EMPTY)
	private String multifieldKey;

	private String planName;

	private Map<String,Object> benefits;

	@Getter
	private Map<String,Object> planBenefitsSummary;

	private Map<String,Object> button;

    private String planBenefitsAsJson;

	@PostConstruct
	protected void init() {
		String pageUrl = request.getPathInfo();
		Optional<Plan> plan = plansService.loadPlan(pageUrl);
		plan.ifPresent(this::mapToModel);

	}

	private void mapToModel(Plan plan) {
		if (null != plan.getSelectedOption()) {
			PlanOption selectedOption = plan.getSelectedOption();
			Map<String, Object > data = selectedOption.getData();
			planName = selectedOption.getName();
			benefits = mapBenefits(data, benefitKey);
			if (StringUtils.isNotBlank(multifieldKey)){
				this.benefits.put(multifieldKey, MultifieldMapper.map(benefits, multifieldKey));
			}
			planBenefitsSummary = mapBenefits(data, "planBenefitsSummary");
            try {
                planBenefitsAsJson = JsonUtil.mapToJson(this, false);
            } catch (JsonProcessingException e) {
                logger.error("Can't serialize {} to JSON", this, e);
                planBenefitsAsJson = StringUtils.EMPTY;
            }
			if (containsButton) {
				button = mapBenefits(data, "button");
			}
		}
	}

	private Map<String, Object> mapBenefits(Map<String, Object> data, String key) {
		Map<String, Object> benefitsMap = Collections.emptyMap();
		ObjectMapper objectMapper = new ObjectMapper();
		if(data != null && data.containsKey(key)){
			Object benefitObject = data.get(key);
			if (null != benefitObject) {
				benefitsMap = objectMapper.convertValue(benefitObject, new TypeReference<Map<String, Object>>() {});
			}
		}
		return benefitsMap;
	}

	public String getBenefitKey() {
		return benefitKey;
	}

	public String getPlanName() {
		return planName;
	}

	public Map<String, Object> getBenefits() {
		return benefits;
	}

	public Boolean getContainsButton() {
		return containsButton;
	}

	public Map<String, Object> getButton() {
		return button;
	}
    @JsonIgnore
    public String getPlanBenefitsAsJson() {
        return planBenefitsAsJson;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return benefits == null || benefits.isEmpty() ;
    }
}
