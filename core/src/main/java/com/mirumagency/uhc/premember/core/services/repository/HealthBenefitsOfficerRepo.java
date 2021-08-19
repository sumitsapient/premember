package com.mirumagency.uhc.premember.core.services.repository;

import com.mirumagency.uhc.premember.core.domain.federal.HealthBenefitsOfficer;

import java.util.List;

public interface HealthBenefitsOfficerRepo {
    List<HealthBenefitsOfficer> load(String regionCode);
}
