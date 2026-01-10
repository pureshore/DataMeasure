package com.hik.test.data.utils;

import com.hik.test.data.dto.entity.ConfigResponse;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigLayoutUtil {

    public static Map<String, List<ConfigResponse.ConfigItem>> groupByLayout(List<ConfigResponse.ConfigItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getLayout() != null ? item.getLayout() : "single",
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    // 按布局类型排序
    public static List<ConfigResponse.ConfigItem> sortByLayout(List<ConfigResponse.ConfigItem> items) {
        Map<String, Integer> layoutOrder = Map.of(
                "single", 1,
                "double", 2,
                "triple", 3
        );

        return items.stream()
                .sorted(Comparator.comparingInt(item ->
                        layoutOrder.getOrDefault(item.getLayout(), 4)))
                .collect(Collectors.toList());
    }
}