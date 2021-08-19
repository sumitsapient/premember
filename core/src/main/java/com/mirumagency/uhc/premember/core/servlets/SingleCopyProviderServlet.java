package com.mirumagency.uhc.premember.core.servlets;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

@Component(service = Servlet.class, immediate = true,
	property = {
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/premember/dialog/dropdown/single-plan-copy"
	})
@ServiceDescription("Single Plan Copy Provider Servlet")
public class SingleCopyProviderServlet extends AbstractCopyProviderServlet {
	private static final String  AFTER_DEDUCTIBLE="tieredPlanCopyOptionAfterDeductible";
	private static final String  DEDUCTIBLE_NOT_APPLY="tieredPlanCopyOptionDeductibleDoesNotApply";
	private static final String  COPAY_WITH_REFERRAL="tieredPlanCopyOptionCopayPlus";
	private static final String  COPAY_WITHOUT_REFERRAL="tieredPlanCopyOptionCopayPlusWithoutReferral";


	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
		throws ServletException, IOException {

		super.doGet(request, response);
	}

	List<String> getContentFragmentModelProperties() {
		return ImmutableList.of(AFTER_DEDUCTIBLE, DEDUCTIBLE_NOT_APPLY,
			COPAY_WITH_REFERRAL,COPAY_WITHOUT_REFERRAL);
	}
}
