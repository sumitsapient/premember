package com.mirumagency.uhc.premember.core.models.federal.health;

import com.mirumagency.uhc.premember.core.domain.federal.PlanPDFFiles;
import com.mirumagency.uhc.premember.core.models.federal.HeadingConfig;
import com.mirumagency.uhc.premember.core.services.federal.HealthDocumentsAndResourcesService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = HealthPlanDocumentsAndResources.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HealthPlanDocumentsAndResources {

    private List<PlanPDFFiles> pdfFilesListSections;

    @Getter
    @ValueMapValue
    private String title;

    @Inject
    private HealthDocumentsAndResourcesService healthDocumentsAndResourcesService;

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {
        Cookie regionCodeCookie = request.getCookie("federal-region-code");
        if (regionCodeCookie != null) {
            String regionCode = regionCodeCookie.getValue();
            pdfFilesListSections = healthDocumentsAndResourcesService.loadHealthDocumentsAndResources(regionCode,
                    request.getPathInfo());
        }
    }

    public List<PlanPDFFiles> getPdfFilesListSections() {
        return pdfFilesListSections;
    }
}
