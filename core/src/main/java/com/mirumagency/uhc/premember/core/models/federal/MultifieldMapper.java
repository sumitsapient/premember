package com.mirumagency.uhc.premember.core.models.federal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.util.Asserts.notNull;

public class MultifieldMapper {

    public static Map<String, Object> map(PlanOption planOption, String key) {
        notNull(planOption, "planOption");
        Map<String, Object> data = planOption.getData();
        return map(data,key);
    }

    public static Map<String, Object> map(Map<String, Object> data, String key) {
        notNull(key, "key");
        Map<String, Object> multifieldMainMap = new HashMap<>();

        if (data.containsKey(key)) {
            Object multifieldNode = data.get(key);
            if (null != multifieldNode) {
                Map<String, Object> multifieldMap = convertToMap(multifieldNode);
                multifieldMap.forEach((itemKey, itemValue) -> getItems(itemKey, itemValue, multifieldMainMap));
            }
        }
        return multifieldMainMap;
    }

    private static  Map<String, Object> convertToMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    private static void getItems(String key, Object value, Map<String, Object> mainMap) {
        if (value instanceof Map) {
            Map<String, Object> multifieldItemsMap = convertToMap(value);
            List<Map> properties = new ArrayList<>();
            multifieldItemsMap.forEach((mimKey, mimVal) -> {
                Map<String, Object> propertiesMap = convertToMap(mimVal);
                properties.add(propertiesMap);
            });
            mainMap.put(key, properties);
        } else {
            mainMap.put(key, value);
        }
    }

}
