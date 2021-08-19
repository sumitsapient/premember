package com.mirumagency.uhc.premember.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = EmployerPlansList.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerPlansList extends Component {

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private String planType;

    private List<Resource> plans;

    @PostConstruct
    protected void initModel() {
        populatePlans();
    }

    private void populatePlans() {
        Stream<Resource> childResourcesStream = Optional.of(resource.getChildren())
                .map(Iterable::iterator)
                .map(this::convertIteratorToStream)
                .orElse(Stream.empty());

        plans = childResourcesStream
                .filter(this::isEmployerPlanResourceType)
                .collect(toList());
    }

    private Stream<Resource> convertIteratorToStream(Iterator<Resource> resourceIterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(resourceIterator, Spliterator.ORDERED),
                false);
    }

    private boolean isEmployerPlanResourceType(Resource resource) {
        return String.format("premember/components/configuration/%sPlanOption", planType).equals(resource.getResourceType());
    }

    public boolean isAnyPlanAdded() {
        return plans != null && !plans.isEmpty();
    }

    public Integer getPlanCount() {
        return plans.size();
    }

    public String getPlanType() {
        return StringUtils.capitalize(planType);
    }
}
