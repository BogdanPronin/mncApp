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
    public static void checkValidTime(String startOfTheLesson,String endOfTheLesson){
        LocalTime start = LocalTime.parse(startOfTheLesson);
        LocalTime end = LocalTime.parse(endOfTheLesson);
        if(start.equals(end)){
            throw new WebException("Start of the lesson cannot coincide with the end",400);
        }else if(!start.isBefore(end)){
            throw new WebException("Wrong time format start of the lesson must be before the end",400);
        }
    }
    public static void checkForOverlappingTime(String startOfTheFirstGroupsLesson,String endOfTheFirstGroupsLesson,String startOfTheSecondGroupsLesson,String endOfTheSecondGroupsLesson){
        LocalTime start1 = LocalTime.parse(startOfTheFirstGroupsLesson);
        LocalTime end1 = LocalTime.parse(endOfTheFirstGroupsLesson);
        LocalTime start2 = LocalTime.parse(startOfTheSecondGroupsLesson);
        LocalTime end2 = LocalTime.parse(endOfTheSecondGroupsLesson);
        if((end1.isBefore(start2) || end1.equals(start2)) || (end2.isBefore(start1) || end2.equals(start1))){
        }else throw new WebException("At this time, another group is engaged",400);

    }
}
