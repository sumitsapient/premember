package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

public abstract class Path {
    public abstract String path();

    @Override
    public String toString() {
        return path();
    }
}
