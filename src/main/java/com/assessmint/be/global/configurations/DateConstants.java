package com.assessmint.be.global.configurations;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class DateConstants {
   public static String pattern = "yyyy-MM-dd";
   public static SimpleDateFormat fmtr = new SimpleDateFormat(pattern);
   public static DateTimeFormatter localDateFmtr = DateTimeFormatter.ofPattern(pattern);
}
