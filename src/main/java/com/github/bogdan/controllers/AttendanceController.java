package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddAttendance;
import com.github.bogdan.modals.Attendance;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.github.bogdan.serializer.UserForGroupSerializer;
import com.github.bogdan.serializer.UserGetSerializer;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;

import static com.github.bogdan.controllers.UserGroupController.checkUserInGroup;
import static com.github.bogdan.services.AttendanceService.checkUniqueAttendance;
import static com.github.bogdan.services.AuthService.*;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.UserService.*;

public class AttendanceController {
    public static void add(Context ctx, Dao<Attendance,Integer> attendanceDao) throws SQLException, JsonProcessingException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()== Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(Attendance.class,new DeserializerForAddAttendance());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            Attendance attendance = objectMapper.readValue(ctx.body(),Attendance.class);
            checkUniqueAttendance(attendance);
            attendanceDao.create(attendance);
            created(ctx);
        }else youAreNotAdmin(ctx);
    }
    public static void delete(Context ctx, Dao<Attendance,Integer> attendanceDao) throws SQLException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()== Role.ADMIN){
            int id = Integer.parseInt(ctx.pathParam("id"));
            attendanceDao.deleteById(id);
            deleted(ctx);
        }else youAreNotAdmin(ctx);
    }
    public static void get(Context ctx, Dao<Attendance,Integer> attendanceDao) throws SQLException, JsonProcessingException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()== Role.ADMIN){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(User.class, new UserForGroupSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);

            ctx.result(objectMapper.writeValueAsString(attendanceDao.queryForAll()));
            ctx.status(200);
        }else youAreNotAdmin(ctx);
    }
    public static void getById(Context ctx, Dao<Attendance,Integer> attendanceDao) throws SQLException, JsonProcessingException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        checkAuthorization(login,password,ctx);
        if(getUserByLogin(login).getRole()== Role.ADMIN){
            ObjectMapper objectMapper = new ObjectMapper();
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(objectMapper.writeValueAsString(attendanceDao.queryForId(id)));
            ctx.status(200);
        }else youAreNotAdmin(ctx);
    }
//    public static void change(Context ctx, Dao<Attendance,Integer> attendanceDao) throws SQLException, JsonProcessingException {
//        String login = ctx.basicAuthCredentials().getUsername();
//        String password = ctx.basicAuthCredentials().getPassword();
//        checkAuthorization(login,password,ctx);
//        if(getUserByLogin(login).getRole()== Role.ADMIN){
//            SimpleModule simpleModule = new SimpleModule();
//            simpleModule.addDeserializer(Attendance.class,new DeserializerForAddAttendance());
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(simpleModule);
//            Attendance attendance = objectMapper.readValue(ctx.body(),Attendance.class);
//            int id = Integer.parseInt(ctx.pathParam("id"));
//            attendance.setId(id);
//            attendanceDao.update(attendance);
//            updated(ctx);
//        }else youAreNotAdmin(ctx);
//    }
}
