package com.itheima.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class FlexibleLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-M-d")
            .optionalStart()
            .appendPattern("'T'HH:mm:ss")
            .optionalEnd()
            .toFormatter();

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            // 如果包含时间部分，只取日期部分
            if (dateStr.contains("T")) {
                dateStr = dateStr.substring(0, dateStr.indexOf("T"));
            }
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (Exception e) {
            // 尝试使用默认格式
            try {
                return LocalDate.parse(dateStr);
            } catch (Exception ex) {
                throw new IOException("无法解析日期: " + dateStr + ", 错误: " + ex.getMessage(), e);
            }
        }
    }
}
