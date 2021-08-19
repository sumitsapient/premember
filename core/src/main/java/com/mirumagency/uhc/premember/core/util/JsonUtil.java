package com.mirumagency.uhc.premember.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public static String mapToJson(Object object, boolean excludeNullValues) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (excludeNullValues) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return mapper.writeValueAsString(object);
    }
}