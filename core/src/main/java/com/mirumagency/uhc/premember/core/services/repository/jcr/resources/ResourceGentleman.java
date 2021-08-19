package com.mirumagency.uhc.premember.core.services.repository.jcr.resources;

import java.util.function.Function;

public interface ResourceGentleman {

    <T> T withResolver(Function<NiceResourceResolver, T> function);

}