package com.hik.test.data.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JsonPathUtils {
    public static Object extractValue(String json, String jsonPath) {
        return JsonPath.read(json, jsonPath);
    }

    /**
     * json 修改
     * @param json
     * @param jsonPath
     * @param newValue
     * @param type 修改的类型，如：SET, DELETE
     * @return
     */
    public static String modifyJson(String json, String jsonPath,
                                    Object newValue, String type) {
        DocumentContext documentContext = JsonPath.parse(json);
        try {
            switch (type) {
                case "SET":
                    documentContext.set(jsonPath, newValue);
                    break;
                case "DELETE":
                    documentContext.delete(jsonPath);
                    break;
//                case "ADD":
//                    // 添加数组元素等操作
//                    documentContext.add(jsonPath, newValue);
//                    break;
            }
        } catch (PathNotFoundException e) {
            return json;
        }
        return documentContext.jsonString();
    }
}
