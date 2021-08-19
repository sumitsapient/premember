package com.mirumagency.uhc.premember.core.services.repository;

import com.mirumagency.uhc.premember.core.domain.Employer;

public interface EmployerRepo {
    Employer load(String resourcePath);
}
