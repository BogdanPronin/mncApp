package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.MyException;
import com.github.bogdan.modals.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;

public class UserService {
    public static User getUserByLogin(String login) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for (User u:userDao.queryForAll()){
            if(u.getLogin().equals(login)){
                return u;
            }
        }
        return null;
    }
    public static void validatePhone(User u, Context ctx) throws NumberParseException {
        if (!isPhoneAvailable(u.getPhone())){
            ctx.status(400);
            throw new MyException("Phone isn't available");
        }
    }
    public static void validateEmail(User u,Context ctx){
        if(!isEmailAvailable(u.getEmail())){
            ctx.status(400);
            throw new MyException("Email isn't available");
        }
    }
    public static void isEmailAlreadyInUse(User u,Context ctx) throws SQLException {
        if(isUserWithSuchEmailAlreadyExists(u.getEmail())){
            ctx.status(400);
            throw new MyException("This mail is already in use");
        }
    }
    public static void isLoginAlreadyInUse(User u,Context ctx) throws SQLException {
        if(isUserWithSuchLoginAlreadyExists(u.getLogin())){
            ctx.status(400);
            throw new MyException("User with such login already exist");
        }
    }
    public static void isPhoneAlreadyInUse(User u,Context ctx) throws SQLException {
        if(isUserWithSuchPhoneAlreadyExists(u.getPhone())){
            ctx.status(400);
            throw new MyException("This phone is already in use");
        }
    }
    public static boolean isEmailAvailable(String email){
        email = email.trim();
        EmailValidator eValidator = EmailValidator.getInstance();
        if(eValidator.isValid(email)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isPhoneAvailable(String swissNumberStr) throws NumberParseException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, null);
        return (phoneUtil.isValidNumber(swissNumberProto));
    }
    public static boolean isUserWithSuchLoginAlreadyExists(String login) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }
    public static boolean isUserWithSuchEmailAlreadyExists(String email) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
    public static boolean isUserWithSuchPhoneAlreadyExists(String phone) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getPhone().equals(phone)){
                return true;
            }
        }
        return false;
    }

    public static User getUserWithSuchLogin(String login) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getLogin().equals(login)){
                return u;
            }
        }
        return null;
    }
    public static User getUserWithSuchEmail(String email) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
    }
    public static User getUserWithSuchPhone(String phone) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getPhone().equals(phone)){
                return u;
            }
        }
        return null;
    }
}
