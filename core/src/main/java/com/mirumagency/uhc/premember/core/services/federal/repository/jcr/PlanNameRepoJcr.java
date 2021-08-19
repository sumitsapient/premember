package com.mirumagency.uhc.premember.core.services.federal.repository.jcr;

import com.adobe.acs.commons.genericlists.GenericList;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = PlanNameRepo.class)
public class PlanNameRepoJcr implements PlanNameRepo {

    @Reference
    private ResourceGentleman resourceGentleman;
    private static final String PLAN_TYPES_PATH = "/etc/acs-commons/lists/federal/plan-names/%s-plan-names";

    @Override
    public List<PlanName> loadPlanTypes(PlanType planType) {
        String listPagePath = String.format(PLAN_TYPES_PATH, planType.getTypeName());
        return resourceGentleman.withResolver(resolver ->
                resolver.getPageManager()
                        .map(pageManager -> pageManager.getPage(listPagePath))
                        .map(page -> page.adaptTo(GenericList.class))
                        .map(genericList -> genericList.getItems()
                                .stream()
                                .map(item -> new PlanName(item.getValue(), item.getTitle()))
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList())
        );
    }
}
