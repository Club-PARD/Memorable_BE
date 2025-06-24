package com.study.memorable.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListIntegerConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return attribute == null
                ? null
                : attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isEmpty() ? Arrays.asList(0, 0) : Arrays.stream(dbData.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
