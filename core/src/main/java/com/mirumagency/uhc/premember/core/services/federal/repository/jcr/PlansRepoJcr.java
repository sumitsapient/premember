package com.mirumagency.uhc.premember.core.services.federal.repository.jcr;

import com.day.cq.commons.jcr.JcrConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.SvgService;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlansRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlansListPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResourceResolver;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import static com.google.common.collect.ImmutableList.of;

import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.MEDICARE;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.VISION;
import static java.util.stream.Collectors.toList;

@Component(service = PlansRepo.class)
public class PlansRepoJcr implements PlansRepo {

    @Reference
    private ResourceGentleman resourceGentleman;

    @Reference
    private SvgService svgService;

    @Reference
    private PlanNameRepo planNameRepo;

    private static final List<String> JCR_RELATED_PROPERTIES =
            Lists.newArrayList(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.JCR_MIXINTYPES,
                    JcrConstants.JCR_LASTMODIFIED, JcrConstants.JCR_LAST_MODIFIED_BY);

    private static final List<PlanType> PLAN_TYPES = of(
            HEALTH,
            DENTAL,
            VISION,
            MEDICARE
    );

    public static final List<String> FEDERAL_PLAN_RESOURCE_TYPES = PLAN_TYPES.stream()
            .map(type -> String
                    .format("premember/components/federal/configuration/%sPlanOption", type.getTypeName()))
            .collect(toList());


    @Override
    public PlanType getPlanType(String pageUrl) {
        PlanPath planPath = new PlanPath(pageUrl);
        return planPath.getPlanType();
    }

    @Override
    public Optional<Plan> loadPlan(String pageUrl) {
        EmployerPath employerPath = EmployerPath.of(pageUrl);
        PlanPath planPath = new PlanPath(pageUrl);
        return resourceGentleman.withResolver(resolver ->
                Optional.of(loadPlan(employerPath, planPath, resolver)));
    }

    @Override
    public String getPlanName(String planType, String planId) {
        PlanType planTypeObject = PlanType.from(planType).orElse(PlanType.NOT_SELECTED);
        List<PlanName> planNames = planNameRepo.loadPlanTypes(planTypeObject);
        return planNames.stream()
                .filter(name -> name.getId().equals(planId))
                .findFirst()
                .map(PlanName::getName)
                .orElse(planId);
    }

    private Plan loadPlan(EmployerPath employerPath, PlanPath planPath,
                          NiceResourceResolver resolver) {
        PlanType type = planPath.getPlanType();
        List<PlanOption> options = getPlanOptions(employerPath, type, resolver);
        PlanOption selectedOption = options
                .stream()
                .filter(op -> op.getId().equals(planPath.getPlanOptionName()))
                .findFirst()
                .orElse(null);
        PlansListPath plansListPath = employerPath.plansListPath(type);
        NiceResource plansResource = resolver.getResource(plansListPath.path())
                .orElseThrow(() -> new IllegalStateException("Plan " + type.getTypeName() + " not configured yet."));
        Map<String,Object> planData = getProperties(plansResource);
        String planHeaderName = planData.get("headerPlanName").toString();
        String planIconLink = svgService.getSvgContentFromAssetPath(planData.get("planIconLink").toString());
        Map<String, Object> labels = getLabels(planData);
        return Plan
                .builder()
                .type(type)
                .selectedOption(selectedOption)
                .options(options)
                .planHeaderName(planHeaderName)
                .iconLink(planIconLink)
                .labels(labels)
                .build();
    }

    private List<PlanOption> getPlanOptions(EmployerPath path, PlanType planType, NiceResourceResolver resolver) {
        List<PlanName> planNames = planNameRepo.loadPlanTypes(planType);
        return plansStream(path.plansListPath(planType), resolver)
                .filter(this::isPlanConfiguration)
                .map(resource -> mapToPlanOption(resource, planNames, planType))
                .collect(toList());
    }

    private PlanOption mapToPlanOption(NiceResource planConfig, List<PlanName> planNames, PlanType planType) {
        String planId = planConfig.getString("id");
        String planName = planNames
                .stream()
                .filter(name -> name.getId().equals(planId))
                .findFirst()
                .map(PlanName::getName)
                .orElse(planId);
        Map<String, Object> data = getPlanOptionData(planConfig);

        return PlanOption
                .builder()
                .id(planId)
                .name(planName)
                .description(planConfig.getString("description"))
                .detailsDescription(planConfig.getString("detailsDescription"))
                .featuredText(planConfig.getString("featuredText"))
                .planBenefitsSummaryPdf(planConfig.getString("planBenefitsSummaryPdf"))
                .data(data)
                .type(planType)
                .build();
    }

    private Stream<NiceResource> plansStream(PlansListPath plansListPath,
                                             NiceResourceResolver resolver) {
        return resolver.getResource(plansListPath.path()).map(NiceResource::getChildren).orElse(Stream.empty());
    }

    private boolean isPlanConfiguration(NiceResource planResource) {
        return FEDERAL_PLAN_RESOURCE_TYPES.contains(planResource.getResourceType());
    }

    private Map<String, Object> getPlanOptionData(NiceResource planResource) {
        Map<String, Object> itemMap = new HashMap<>();

        planResource.getChildren().forEach(benefits -> {
            Map<String, Object> benefitsMap = getProperties(benefits);
            itemMap.put(benefits.getName(), benefitsMap);
        });

        return itemMap;
    }

    private Map<String, Object> getProperties(NiceResource item) {
        Map<String, Object> properties = item.getValueMap().entrySet().stream()
                .filter(entry -> !JCR_RELATED_PROPERTIES.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        item.getChildren().forEach(resource -> {
            properties.put(resource.getName(), getProperties(resource));
        });
        return properties;
    }

    private Map<String, Object> getLabels(Map<String, Object> data) {
        Map<String, Object> labelsMap = Collections.emptyMap();
        ObjectMapper objectMapper = new ObjectMapper();
        if(data != null && data.containsKey("labels")){
            Object benefitObject = data.get("labels");
            if (null != benefitObject) {
                labelsMap = objectMapper.convertValue(benefitObject, new TypeReference<Object>() {});
            }
        }
        return labelsMap;
    }

}
