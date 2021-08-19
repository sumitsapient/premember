package com.mirumagency.uhc.premember.core.services.repository;

import com.mirumagency.uhc.premember.core.domain.plans.Plans;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;

public interface PlansRepo {

    Plans loadAll(EmployerPath path);
}
