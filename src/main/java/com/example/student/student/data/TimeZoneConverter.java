package com.example.student.student.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.TimeZone;

@Converter
public class TimeZoneConverter implements AttributeConverter<TimeZone, String> {

    @Override
    public String convertToDatabaseColumn(TimeZone attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getID();
    }

    @Override
    public TimeZone convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TimeZone.getTimeZone(dbData);
    }
}