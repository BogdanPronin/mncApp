package com.github.bogdan.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.DeserializerForChangeSchedule;
import com.github.bogdan.deserializer.ScheduleDeserializer;
import com.github.bogdan.models.Role;
import com.github.bogdan.models.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import static com.github.bogdan.services.AuthService.authorization;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.PaginationService.getPage;
import static com.github.bogdan.services.ScheduleService.*;
import static com.github.bogdan.services.UserService.getUserByLogin;

public class ScheduleController {
    public static void add(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                checkDoesRequestBodyEmpty(ctx);
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Schedule.class,new ScheduleDeserializer());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(simpleModule);

                Schedule s = objectMapper.readValue(ctx.body(),Schedule.class);
                checkDoesThisSchedulePossible(s);
                checkDoesSuchScheduleRecordExist(s);
                scheduleDao.create(s);
                created(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);

    }
    public static void delete(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException {
        checkDoesBasicAuthEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                int id = Integer.parseInt(ctx.pathParam("id"));
                checkDoesScheduleWithSuchIdExists(id);
                scheduleDao.deleteById(id);
                deleted(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void get(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                ObjectMapper objectMapper = new ObjectMapper();
                checkDoesQueryParamEmpty(ctx,"page");
                checkDoesQueryParamEmpty(ctx,"size");
                int page = Integer.parseInt(ctx.queryParam("page"));
                int size = Integer.parseInt(ctx.queryParam("size"));
                ctx.result(objectMapper.writeValueAsString(getPage(scheduleDao,page,size)));
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void getById(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                ObjectMapper objectMapper = new ObjectMapper();
                int id = Integer.parseInt(ctx.pathParam("id"));
                checkDoesScheduleWithSuchIdExists(id);
                ctx.result(objectMapper.writeValueAsString(scheduleDao.queryForId(id)));
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    public static void change(Context ctx, Dao<Schedule,Integer> scheduleDao) throws SQLException, JsonProcessingException {
        checkDoesBasicAuthEmpty(ctx);
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()== Role.ADMIN){
                checkDoesRequestBodyEmpty(ctx);
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(Schedule.class,new DeserializerForChangeSchedule());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(simpleModule);

                Schedule s = objectMapper.readValue(ctx.body(),Schedule.class);
                Logger logger = LoggerFactory.getLogger(Schedule.class);
                logger.info(s.toString());
                checkDoesThisSchedulePossibleExceptOne(s);
                checkDoesSuchScheduleRecordExist(s);
                scheduleDao.update(s);
                updated(ctx);
            }else youAreNotAdmin(ctx);
        }else authorizationFailed(ctx);
    }
    //возвращает лист расписаний данной группы
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
