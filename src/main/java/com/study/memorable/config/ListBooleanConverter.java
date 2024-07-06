package com.study.memorable.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListBooleanConverter implements AttributeConverter<List<Boolean>, String> {

    @Override
    public String convertToDatabaseColumn(List<Boolean> attribute) {
        return attribute != null ? attribute.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
    }

    @Override
    public List<Boolean> convertToEntityAttribute(String dbData) {
        return dbData != null && !dbData.isEmpty()
                ? Arrays.stream(dbData.split(",")).map(Boolean::valueOf).collect(Collectors.toList())
                : Arrays.asList(false, false);
    }
}
