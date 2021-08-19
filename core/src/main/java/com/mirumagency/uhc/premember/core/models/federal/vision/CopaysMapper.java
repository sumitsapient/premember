package com.mirumagency.uhc.premember.core.models.federal.vision;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.Copays;

import java.util.Map;

public class CopaysMapper {
    public static Copays map(PlanOption planOption) {
        Object costs = planOption.getData().get("copays");
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> planCosts = oMapper.convertValue(costs, new TypeReference<Map<String, Object>>() {});
        Double copayAnnualExams = Double.parseDouble(planCosts.get("annualEyeExam").toString());
        Double copayGlasses = Double.parseDouble(planCosts.get("eyeGlasses").toString());
        return new Copays(copayAnnualExams, copayGlasses);
    }
}
