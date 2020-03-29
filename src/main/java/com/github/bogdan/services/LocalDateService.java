package com.github.bogdan.services;

import com.github.bogdan.exceptions.WebException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateService {
    public static LocalDate getLocalDateByString(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        return parsedDate;
    }
    public static void checkLocalDateFormat(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate parsedDate = LocalDate.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Wrong date format, correct date format should be YYYY-MM-DD"+'\n'+e.getMessage(),400);
        }
    }
    public static void checkLocalDateTimeFormat(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime parsedDate = LocalTime.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Wrong time format, correct date format should be HH:mm"+'\n'+e.getMessage(),400);
        }
    }
}
