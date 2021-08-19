package com.mirumagency.uhc.premember.core.services.repository.jcr;

import static com.google.common.collect.ImmutableList.of;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.BEHAVIORAL_HEALTH;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.FINANCIAL;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.FSA;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.PHARMACY;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.VISION;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.*;
import com.mirumagency.uhc.premember.core.domain.plans.decorators.copy.SummaryTopicIconDecorator;
import com.mirumagency.uhc.premember.core.domain.plans.decorators.planoption.PlanOptionDetailsWithEmployerConfigDecorator;
import com.mirumagency.uhc.premember.core.services.SvgService;
import com.mirumagency.uhc.premember.core.services.repository.PlansRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanOptionPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlansListPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResourceResolver;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PlansRepo.class)
public class PlansRepoJcr implements PlansRepo {

  private static final List<PlanType> PLAN_TYPES = of(
      HEALTH,
      DENTAL,
      VISION,
      BEHAVIORAL_HEALTH,
      FSA,
      FINANCIAL,
      PHARMACY
  );

  public static final List<String> EMPLOYER_PLAN_RESOURCE_TYPES = PLAN_TYPES.stream()
      .map(type -> String
          .format("premember/components/configuration/%sPlanOption", type.getTypeName()))
      .collect(toList());

  public static final String CUSTOM_PDLS_NODE_NAME = "customPdls";

  @Reference
  private ResourceGentleman resourceGentleman;

  @Reference
  private SvgService svgService;

  @Reference
  private ContentRepoJcr contentRepo;

  @Override
  public Plans loadAll(EmployerPath path) {
    return resourceGentleman.withResolver(resolver -> {
      List<Plan> plans = PLAN_TYPES.stream()
          .map(type -> loadByType(path, type, resolver))
          .collect(toList());
      return new Plans(plans);
    });
  }

  private Plan loadByType(EmployerPath path, PlanType type, NiceResourceResolver resolver) {
    PlanCopy copy = contentRepo.loadPlanCopy(type, path);
    List<PlanOption> options = getPlanOptionsList(path, type, copy, resolver);
    List<PlanPdl> customPdls = null;
    if (type.equals(PHARMACY)){
      customPdls = getCustomPdlsList(path, type, copy, resolver);
    }
    return Plan.builder()
        .type(type)
        .options(options)
        .customPdls(customPdls)
        .copy(new SummaryTopicIconDecorator(copy, svgService))
        .build();
  }

  private List<PlanOption> getPlanOptionsList(EmployerPath path, PlanType type,
      PlanCopy copy,
      NiceResourceResolver resolver) {
    return plansStream(path.plansListPath(type), resolver)
        .filter(this::isPlanConfiguration)
        .map(planConfig -> resolveFromPlanConfigResource(path, planConfig, copy))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
  }

  private Stream<NiceResource> plansStream(PlansListPath plansListPath,
      NiceResourceResolver resolver) {
    NiceResource plansListResource = resolver.getResourceOrThrow(plansListPath.path());
    return plansListResource.getChildren();
  }

  private boolean isPlanConfiguration(NiceResource planResource) {
    return EMPLOYER_PLAN_RESOURCE_TYPES.contains(planResource.getResourceType());
  }

  private Optional<PlanOption> resolveFromPlanConfigResource(EmployerPath employerPath, NiceResource planConfig,
      PlanCopy copy) {
    Optional<PlanOptionPath> pathOptional = PlanOptionPath.from(planConfig);
    if (pathOptional.isPresent()) {
      PlanOptionPath path = pathOptional.get();
      Map<String, Object> data = contentRepo.loadData(path.path(), employerPath);
      return Optional.of(extractPlanOptionFromPath(path, copy, data, planConfig));
    }
    return Optional.empty();
  }

  private PlanOption extractPlanOptionFromPath(
      PlanOptionPath path,
      PlanCopy copy,
      Map<String, Object> data,
      NiceResource planConfig) {

    return PlanOption.builder()
        .type(path.extractPlanType())
        .name(path.extractPlanName())
        .variation(path.extractPlanVariation())
        .details(new PlanOptionDetailsWithEmployerConfigDecorator(
            copy,
            data,
            path.extractPlanType(),
            planConfig))
        .build();
  }

  private List<PlanPdl> getCustomPdlsList(EmployerPath path, PlanType type,
                                              PlanCopy copy,
                                              NiceResourceResolver resolver) {
    return plansStream(path.plansListPath(type), resolver)
            .filter(this::isPlanConfiguration)
            .map(planConfig -> getPlanPdlsFromConfig(planConfig, CUSTOM_PDLS_NODE_NAME))
            .findFirst().orElse(null);
  }

  private List<PlanPdl> getPlanPdlsFromConfig(NiceResource planConfigResource, String resourceName) {
    Optional<NiceResource> resource = planConfigResource.getChild(resourceName);
    if (resource.isPresent()) {
        return resource.get().getChildren()
                .map(niceResource -> PlanPdl.of(niceResource.getValueMap()))
                .collect(Collectors.toList());
    }
    return ImmutableList.of();
  }
}