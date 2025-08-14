package com.example.flightapi.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtils {

    private static final ObjectMapper objectMapper = ApplicationContextUtils.getBean(ObjectMapper.class);

    public static String toJson(Object obj) {
        String json;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            json = "";
        }

        return json;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T toObject(String json, TypeReference<T> type) {
        T result = null;
        try {
            result = objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        List<T> result = null;
        try {
            result = objectMapper.readerForListOf(clazz).readValue(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
