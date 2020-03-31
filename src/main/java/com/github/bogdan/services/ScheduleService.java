package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.modals.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.LocalTime;

import static com.github.bogdan.services.LocalDateService.*;

public class ScheduleService {
    public static void checkIsThisSchedulePossible(Schedule currentSchedule) throws SQLException {
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
}
