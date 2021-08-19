package com.mirumagency.uhc.premember.core.models.federal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Model(adaptables = SlingHttpServletRequest.class,
		adapters = Plan.class,
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Plan {

	private static final String CATEGORY_INFO_KEYS = "categoryInfoKeys";
	private static final String MULTIFIELD_KEYS = "multifieldKeys";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ObjectMapper oMapper = new ObjectMapper();

	@SlingObject
	private SlingHttpServletRequest request;

	@Inject
	private PlansService plansService;

	private String planName;

	private Map<String,Object> data = new HashMap<>();

	private String planDataAsJson;

	private List<String> categoryInfoKeys = new ArrayList<>();

	private List<String> multifieldKeys = new ArrayList<>();

	@PostConstruct
	protected void init() {
		try {
			String pageUrl = request.getPathInfo();
			Node fullPlanNode = request.getResource().adaptTo(Node.class);
			categoryInfoKeys = fetchKeysFromNode(CATEGORY_INFO_KEYS, fullPlanNode);
			multifieldKeys = fetchKeysFromNode(MULTIFIELD_KEYS, fullPlanNode);
			Optional<com.mirumagency.uhc.premember.core.domain.federal.Plan> plan = plansService.loadPlan(pageUrl);
			plan.ifPresent(this::mapToModel);
		} catch (Exception e) {
			logger.error("Error fetching values from repository " + e.getMessage());
		}
	}

	private List<String> fetchKeysFromNode(String key, Node node) {
		try {
			List<String> keys = new ArrayList<>();
			if (node.hasProperty(key)) {
				if (node.getProperty(key).isMultiple()) {
					Value[] values = node.getProperty(key).getValues();
					keys = Arrays.stream(values).map(this::convertToString).collect(Collectors.toList());
				} else {
					Value value = node.getProperty(key).getValue();
					keys.add(value.getString());
				}
			}
			return keys;
		} catch (Exception e) {
			logger.error("Error fetching values from repository " + e.getMessage());
		}
		return Collections.emptyList();
	}

	private String convertToString(Value value) {
		try {
			return value.getString();
		} catch (RepositoryException e) {
			logger.error("Error fetching value from repository " + e.getMessage());
		}
		return StringUtils.EMPTY;
	}

	private void mapToModel(com.mirumagency.uhc.premember.core.domain.federal.Plan plan) {
		if (null != plan.getSelectedOption()) {
			PlanOption selectedOption = plan.getSelectedOption();
			Map<String, Object> selectedOptionData = selectedOption.getData();
			planName = selectedOption.getName();
			categoryInfoKeys.forEach(key -> this.data.put(key, selectedOptionData.get(key)));
			multifieldKeys.forEach(key -> this.data.put(key, MultifieldMapper.map(selectedOptionData, key)));
			try {
				planDataAsJson = JsonUtil.mapToJson(this, false);
			} catch (JsonProcessingException e) {
				logger.error("Can't serialize {} to JSON", this, e);
				planDataAsJson = StringUtils.EMPTY;
			}
		}
	}

	public String getPlanName() {
		return planName;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}


	@JsonIgnore
	public String getPlanDataAsJson() {
		return planDataAsJson;
	}
}
