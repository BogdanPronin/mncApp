package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.LocalDateService.*;

public class ScheduleService {
    public static void checkDoesThisSchedulePossible(Schedule currentSchedule) throws SQLException {
        Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
        checkLocalDateTimeFormat(currentSchedule.getStartOfTheLesson());
        checkLocalDateTimeFormat(currentSchedule.getEndOfTheLesson());
        checkValidTime(currentSchedule.getStartOfTheLesson(),currentSchedule.getEndOfTheLesson());
        for(Schedule schedule:scheduleDao){
            if(schedule.getCabinet()==currentSchedule.getCabinet() && schedule.getDay()==currentSchedule.getDay()){
                checkForOverlappingTime(schedule.getStartOfTheLesson(),schedule.getEndOfTheLesson(),currentSchedule.getStartOfTheLesson(),currentSchedule.getEndOfTheLesson());
            }
        }
    }
    public static void checkDoesScheduleWithSuchIdExists(int id) throws SQLException {
        Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
        if(scheduleDao.queryForId(id)==null){
            throw new WebException("Schedule with such id doesn't exists",400);
        }
    }
    public static void checkIsThereLessonOnThisDate(String date,int groupId) throws SQLException {
        LocalDate localDate = LocalDate.parse(date);
        boolean isThereLessonOnThisDate =false;
        Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
        for(Schedule schedule:scheduleDao.queryForAll()){
            if(schedule.getGroup().getId()==groupId){
                if(schedule.getDay().equals(localDate.getDayOfWeek())){
                    isThereLessonOnThisDate = true;
                }
            }
        }
        if(!isThereLessonOnThisDate){
            throw new WebException("There no lessons for this group on that date",400);
        }
    }
}
