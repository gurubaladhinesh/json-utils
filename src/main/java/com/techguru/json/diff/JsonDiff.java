package com.techguru.json.diff;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techguru.json.exception.JsonException;
import com.techguru.json.validate.JsonValidate;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface JsonDiff {

    static MapDifference<String, Object> compare(String leftJson, String rightJson) throws JsonException {
        JsonValidate.validate(leftJson);
        JsonValidate.validate(rightJson);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> leftJsonMap = gson.fromJson(leftJson, type);
        Map<String, Object> rightJsonMap = gson.fromJson(rightJson, type);

        Map<String, Object> leftJsonFlatMap = flatten(leftJsonMap);
        Map<String, Object> rightJsonFlatMap = flatten(rightJsonMap);

        MapDifference<String, Object> diff = Maps.difference(leftJsonFlatMap, rightJsonFlatMap);
        return diff;
    }

    private static Map<String, Object> flatten(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .flatMap(JsonDiff::flatten)
                .collect(LinkedHashMap::new, (m, e) -> m.put("/" + e.getKey(), e.getValue()), LinkedHashMap::putAll);
    }

    private static Stream<Map.Entry<String, Object>> flatten(Map.Entry<String, Object> entry) {

        if (entry == null) {
            return Stream.empty();
        }

        if (entry.getValue() instanceof Map<?, ?>) {
            Map<?, ?> properties = (Map<?, ?>) entry.getValue();
            return properties.entrySet()
                    .stream()
                    .flatMap(e -> flatten(new AbstractMap.SimpleEntry<>(entry.getKey() + "/" + e.getKey(), e.getValue())));
        }

        if (entry.getValue() instanceof List<?>) {
            List<?> list = (List<?>) entry.getValue();
            return IntStream.range(0, list.size())
                    .mapToObj(i -> new AbstractMap.SimpleEntry<String, Object>(entry.getKey() + "/" + i, list.get(i)))
                    .flatMap(JsonDiff::flatten);
        }
        return Stream.of(entry);
    }
}
