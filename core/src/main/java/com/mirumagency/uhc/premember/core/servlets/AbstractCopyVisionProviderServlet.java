package com.mirumagency.uhc.premember.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.dto.TieredBenefitsCopy;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanCopyPath;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

 abstract class AbstractCopyVisionProviderServlet extends SlingSafeMethodsServlet {
	private static final String RESOURCE_TYPE = "nt:unstructured";

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
		throws ServletException, IOException {

		ResourceResolver resourceResolver = request.getResourceResolver();
		List<Resource> options = getValueMapResourceList(resourceResolver);

		DataSource ds = new SimpleDataSource(options.iterator());
		request.setAttribute(DataSource.class.getName(), ds);
	}

	private List<Resource> getValueMapResourceList(ResourceResolver resourceResolver) {
		PlanCopyPath planCopyPath = PlanCopyPath.of(PlanType.VISION);
		Resource planCopy = resourceResolver.getResource(planCopyPath.path());
		ValueMap planCopyValueMap = Optional.ofNullable(planCopy)
			.map(Resource::getValueMap)
			.orElse(ValueMap.EMPTY);

		return planCopyValueMap.entrySet()
			.stream()
			.filter(planCopyEntry -> getContentFragmentModelProperties().contains(planCopyEntry.getKey()))
			.map(TieredBenefitsCopy::ofContentFragmentEntry)
			.map(TieredBenefitsCopy::asValueMap)
			.map(listItem -> new ValueMapResource(resourceResolver, new ResourceMetadata(), RESOURCE_TYPE, listItem))
			.collect(Collectors.toList());
	}

	abstract List<String> getContentFragmentModelProperties();
}
