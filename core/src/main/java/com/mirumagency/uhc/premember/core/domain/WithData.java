package com.mirumagency.uhc.premember.core.domain;

import java.util.Map;

public interface WithData {
    Map<String, Object> getData();

    default String getOrEmpty(String prop) {
        return this.getOrDefault(prop, "");
    }

    default boolean contains(String key) {
        return getData().containsKey(key);
    }

    default Object get(String key) {
        return getData().get(key);
    }

    @SuppressWarnings("unchecked")
    default <T> T getOrDefault(String key, T _default) {
        return (T) getData().getOrDefault(key, _default);
    }
}
