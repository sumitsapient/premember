package com.mirumagency.uhc.premember.core.domain.plans.decorators.copy;

import static com.google.common.collect.Maps.newHashMap;

import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.services.SvgService;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class SummaryTopicIconDecorator extends PlanCopyDecorator {

    private static final String ICON_LINK_PROP = "IconLink";

    private final SvgService svgService;

    public SummaryTopicIconDecorator(PlanCopy toDecorate, SvgService svgService) {
        super(toDecorate);
        this.svgService = svgService;
        decorateIfApplicable(getData());
    }

    @Override
    boolean accept(Map<String, Object> toDecorate) {
        return getData().keySet().stream()
            .anyMatch(key -> StringUtils.contains(key, ICON_LINK_PROP));
    }

    @Override
    void decorate(Map<String, Object> toDecorate) {
        HashMap<String, Object> decorated = newHashMap(toDecorate);
        Map<String, String> iconSvgContentEntries = decorated.entrySet().stream()
            .filter(entry -> StringUtils.contains(entry.getKey(), ICON_LINK_PROP))
            .collect(Collectors.toMap(
                entry -> generatePropNameForSvgIcon(entry.getKey()),
                entry -> svgService.getSvgContentFromAssetPath((String) entry.getValue()))
            );
        decorated.putAll(iconSvgContentEntries);
        setData(decorated);
    }

    private String generatePropNameForSvgIcon(String key) {
        return StringUtils.replace(key, "Link", "Content");
    }

}
