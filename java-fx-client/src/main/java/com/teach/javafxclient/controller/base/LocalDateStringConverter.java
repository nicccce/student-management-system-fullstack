package com.teach.javafxclient.controller.base;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * LocalDateStringConverter 实践转换工具类，支持DatePicker的使用
 */
public class LocalDateStringConverter  extends StringConverter<LocalDate> {
    private String pattern = "yyyy-MM-dd";
    private DateTimeFormatter dtFormatter;
    public LocalDateStringConverter() {
        dtFormatter = DateTimeFormatter.ofPattern(pattern);
    }
    public LocalDateStringConverter(String pattern) {
        this.pattern = pattern;
        dtFormatter = DateTimeFormatter.ofPattern(pattern);
    }
    @Override
    public LocalDate fromString(String text) {
        LocalDate date = null;
        if (text != null && !text.trim().isEmpty()) {
            date = LocalDate.parse(text, dtFormatter);
        }
        return date;
    }    @Override
    public String toString(LocalDate date) {
        String text = null;
        if (date != null) {
            text = dtFormatter.format(date);
        }
        return text;
    }
}
