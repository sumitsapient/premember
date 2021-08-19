package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.HealthBenefitsOfficer;
import com.mirumagency.uhc.premember.core.services.repository.HealthBenefitsOfficerRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Component(service = HealthBenefitsOfficerService.class)
public class HealthBenefitsOfficerService {

    @Reference
    HealthBenefitsOfficerRepo healthBenefitsOfficerRepo;

    public List<HealthBenefitsOfficer> loadOfficers(String regionCode){
        return healthBenefitsOfficerRepo.load(regionCode);
    }
}
