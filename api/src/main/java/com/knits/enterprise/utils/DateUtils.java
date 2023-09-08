package com.knits.enterprise.utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate formatDateString(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
}
