package com.mirumagency.uhc.premember.core.domain.plans.decorators.planoption;

import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;

import com.day.cq.commons.jcr.JcrConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOptionDetails;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.SlingConstants;

public class PlanOptionDetailsWithEmployerConfigDecorator extends PlanOptionDetails {

    private static final List<String> NON_PLAN_OPTION_PROPERTIES = Lists.newArrayList(
        "fragmentPath", "variationName", JcrConstants.JCR_LASTMODIFIED,
        JcrConstants.JCR_LAST_MODIFIED_BY, "sling:" + SlingConstants.PROPERTY_RESOURCE_TYPE,
        JcrConstants.JCR_PRIMARYTYPE);

    public PlanOptionDetailsWithEmployerConfigDecorator(PlanCopy copy, Map<String, Object> data, PlanType type,
        NiceResource planConfig) {
        super(copy, decorateData(data, planConfig, type));
    }

    private static Map<String, Object> decorateData(Map<String, Object> data, NiceResource planConfig, PlanType type) {
        Map<String, Object> detailsWithConfigData = Maps.newHashMap(data);
        detailsWithConfigData.putAll(getPropsFromConfig(planConfig));
        return detailsWithConfigData;
    }

    private static Map<String, Object> getPropsFromConfig(NiceResource planConfigResource) {
        return planConfigResource.getValueMap().entrySet().stream()
            .filter(entry -> !NON_PLAN_OPTION_PROPERTIES.contains(entry.getKey()))
            .collect(toMap());
    }
}