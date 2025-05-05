package com.assessmint.be.global.configurations;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class DateConstants {
    public static String pattern = "yyyy-MM-dd";
    public static SimpleDateFormat fmtr = new SimpleDateFormat(pattern);
    public static DateTimeFormatter localDateFmtr = DateTimeFormatter.ofPattern(pattern);

    public static String dateTimePattern = "yyyy-MM-dd HH:mm";
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
}
