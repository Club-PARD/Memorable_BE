package com.study.memorable.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListBooleanConverter implements AttributeConverter<List<Boolean>, String> {
    @Override
    public String convertToDatabaseColumn(List<Boolean> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Boolean> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>(Collections.nCopies(20, false));
        }
        return Arrays.stream(dbData.split("\\s*,\\s*"))
                .map(Boolean::parseBoolean)
                .collect(Collectors.toList());
    }
}
