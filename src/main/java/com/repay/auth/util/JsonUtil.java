package com.repay.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtil {
    private JsonUtil() {
    }

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String getJsonStringFromObject(Object object) {
        ObjectWriter ow = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {

        }
        return json;
    }

}
