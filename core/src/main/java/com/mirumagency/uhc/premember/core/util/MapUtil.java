package com.mirumagency.uhc.premember.core.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapUtil {

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue
        );
    }

    public static <K, V, NV> Map.Entry<K, NV> mapValue(Map.Entry<K, V> entry, Function<V, NV> map) {
        return new AbstractMap.SimpleEntry<>(entry.getKey(), map.apply(entry.getValue()));
    }

    public static <K, V, NK> Map.Entry<NK, V> mapKey(Map.Entry<K, V> entry, Function<K, NK> map) {
        return new AbstractMap.SimpleEntry<>(map.apply(entry.getKey()), entry.getValue());
    }

    public static boolean valueIsABoolean(Map.Entry<String, Object> entry) {
        return valueIsATrueBoolean(entry) || valueIsAFalseBoolean(entry);
    }

    public static boolean valueIsATrueBoolean(Map.Entry<String, Object> entry) {
        return BooleanUtil.isTrue(entry.getValue());
    }

    public static boolean valueIsAFalseBoolean(Map.Entry<String, Object> entry) {
        return BooleanUtil.isFalse(entry.getValue());
    }

    public static Map<String, String> mapValuesToStrings(Map<String, Object> data) {
        return data.entrySet().stream()
            .map(e -> mapValue(e, Object::toString))
            .collect(toMap());
    }

}
