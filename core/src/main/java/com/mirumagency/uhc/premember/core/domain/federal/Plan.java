package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Builder
public class Plan {
    private PlanType type;
    private PlanOption selectedOption;
    private List<PlanOption> options;
    private String planHeaderName;
    private String iconLink;
    private Map<String, Object> labels;
    public Optional<PlanOption> getFirstOption() {
        return getOptions().stream().findFirst();
    }
    public int optionsCount() {
        return getOptions().size();
    }
    public boolean isSelectedOptionAllowed(){
        if(selectedOption == null) {
            return false;
        }
        return options.stream().anyMatch(planOption -> planOption.getId().equals(selectedOption.getId()));
    }
}