package com.mirumagency.uhc.premember.core.services.federal.repository;

import com.mirumagency.uhc.premember.core.domain.federal.PlanPDFFiles;

import java.util.List;

public interface HealthDocumentsAndResourcesRepo {

        List<PlanPDFFiles> load(String regionCode, String pageUrl);
}
