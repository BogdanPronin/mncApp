package com.github.bogdan.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.ScheduleDeserializer;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.ArrayList;
import static com.github.bogdan.services.AuthService.authorization;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.LocalDateService.checkLocalDateTimeFormat;
import static com.github.bogdan.services.UserService.getUserByLogin;

public class ScheduleController {
    public static void add(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException, JsonProcessingException {
        basicAuthIsEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Schedule.class,new ScheduleDeserializer());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(simpleModule);

                Schedule s = objectMapper.readValue(ctx.body(),Schedule.class);

                checkLocalDateTimeFormat(s.getStartOfTheLesson());
                checkLocalDateTimeFormat(s.getEndOfTheLesson());
                scheduleDao.create(s);
                created(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);

    }
    public static void delete(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException {
        basicAuthIsEmpty(ctx);
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                scheduleDao.deleteById(id);
                deleted(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void get(Context ctx, Dao<Schedule,Integer> scheduleDao){}
    public static void getById(Context ctx, Dao<Schedule,Integer> scheduleDao){}
    public static void change(Context ctx, Dao<Schedule,Integer> scheduleDao){}

    public static ArrayList<Schedule> getGroupsSchedule(int groupId) throws SQLException {
        Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
        ArrayList<Schedule> scheduleArrayList = new ArrayList<>();
        for(Schedule schedule: scheduleDao.queryForAll()){
            if(groupId == schedule.getGroup().getId()){
                scheduleArrayList.add(schedule);
            }
        }
        return scheduleArrayList;
    }
}
