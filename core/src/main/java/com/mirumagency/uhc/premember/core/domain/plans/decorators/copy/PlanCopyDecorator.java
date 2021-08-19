package com.mirumagency.uhc.premember.core.domain.plans.decorators.copy;

import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import java.util.Map;

abstract class PlanCopyDecorator extends PlanCopy {

    PlanCopyDecorator(PlanCopy toDecorate) {
        super(toDecorate.getData());
    }

    void decorateIfApplicable(Map<String, Object> toDecorate) {
        if (accept(toDecorate)) {
            decorate(toDecorate);
        }
    }

    abstract boolean accept(Map<String, Object> toDecorate);

    abstract void decorate(Map<String, Object> toDecorate);
}
