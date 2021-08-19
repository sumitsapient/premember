package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.PlanPDFFiles;
import com.mirumagency.uhc.premember.core.services.federal.repository.HealthDocumentsAndResourcesRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Component( service = HealthDocumentsAndResourcesService.class)
public class HealthDocumentsAndResourcesService {

    @Reference
    HealthDocumentsAndResourcesRepo healthDocumentsAndResourcesRepo;

    public List<PlanPDFFiles> loadHealthDocumentsAndResources(String regionCode, String pageUrl) {
        return healthDocumentsAndResourcesRepo.load(regionCode, pageUrl);
    }

}
