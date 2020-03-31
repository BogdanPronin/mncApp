package com.github.bogdan.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForChangeUser;
import com.github.bogdan.exceptions.MyException;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.github.bogdan.serializer.UserGetSerializer;
import com.google.i18n.phonenumbers.NumberParseException;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.services.AuthService.*;
import static com.github.bogdan.services.ContextService.*;
import static com.github.bogdan.services.UserService.*;

public class UserController {
    static ObjectMapper objectMapper = new ObjectMapper();
    public static void add(Context ctx, Dao<User,Integer> userDao) throws JsonProcessingException, NumberParseException, SQLException {
        LocalDate localDate = LocalDate.now();
        String body = ctx.body();
        User u = objectMapper.readValue(body, User.class);
        u.setDateOfRegister(localDate.toString());
        u.setRole(Role.USER);
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12));
        u.setPassword(hashedPassword);
        validateEmail(u,ctx);
        validatePhone(u,ctx);
        isPhoneAlreadyInUse(u,ctx);
        isLoginAlreadyInUse(u,ctx);
        isEmailAlreadyInUse(u,ctx);
        try {userDao.create(u);
            created(ctx);
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new SQLRefactoredException(e.getCause().getMessage());
        }
    }
    public static void changeUser(Context ctx, Dao<User,Integer> userDao) throws SQLException, JsonProcessingException, NumberParseException {
        if(authorization(ctx.basicAuthCredentials().getUsername(),ctx.basicAuthCredentials().getPassword())) {
            String body = ctx.body();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(User.class, new DeserializerForChangeUser());
            ObjectMapper objectMapperForChangeUser = new ObjectMapper();
            objectMapperForChangeUser.registerModule(simpleModule);

            User u = objectMapperForChangeUser.readValue(body, User.class);
            User currentUser = getUserByLogin(ctx.basicAuthCredentials().getUsername());
            validateEmail(u,ctx);
            validatePhone(u,ctx);
            if(currentUser.getRole()==Role.USER){
                if(currentUser.getId()==u.getId()){
                    if(u.getRole()==Role.ADMIN){
                        youAreNotAdmin(ctx);
                        throw new MyException("You can't change your role");
                    }
                }else{
                    youAreNotAdmin(ctx);
                    throw new MyException("You can't change another user");
                }
            }else{
                if(userDao.queryForId(u.getId()).getRole()==Role.ADMIN){
                    if(!u.equals(userDao.queryForId(u.getId()))){
                        ctx.status(401);
                        throw new MyException("You can't change Admin's fields");
                    }
                }
            }
//            if(getUserWithSuchLogin(u.getLogin())!=null){
//                if(getUserWithSuchLogin(u.getLogin()).getId()!=u.getId()){
//                    ctx.status(400);
//                    throw new MyException("User with such login already exist");
//                }
//            }

            userDao.update(u);
            updated(ctx);
        }else authorizationFailed(ctx);

    }
    public static void delete(Context ctx, Dao<User,Integer> userDao) throws SQLException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(authorization(login,password)){
            if(getUserByLogin(login).getRole()==Role.ADMIN){
                if(id != getUserByLogin(login).getId() && userDao.queryForId(id).getRole() == Role.ADMIN){
                    throw new WebException("You can't change Admin's fields",400);
                }else{
                    userDao.deleteById(id);
                    deleted(ctx);
                }
            }else{
                if(getUserByLogin(login).getId()!=id){
                    youAreNotAdmin(ctx);
                }else deleted(ctx);
            }
        }else authorizationFailed(ctx);
    }
    public static void getUser(Context ctx, Dao<User,Integer> userDao) throws SQLException, JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(authorization(login,password)){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(User.class, new UserGetSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            String serialized = objectMapper.writeValueAsString(userDao.queryForId(id));
            ctx.result(serialized);
            ctx.status(200);
        }else authorizationFailed(ctx);
    }
    public static void getUsers(Context ctx, Dao<User,Integer> userDao) throws SQLException, JsonProcessingException {

        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(authorization(login,password)){
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(User.class, new UserGetSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            String serialized = objectMapper.writeValueAsString(userDao.queryForAll());
            ctx.result(serialized);
            ctx.status(200);
        }else authorizationFailed(ctx);
    }
}
