package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.models.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
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
    public static void validatePhone(User u) throws NumberParseException {
        if (!doesPhoneAvailable(u.getPhone())){
            throw new WebException("Phone isn't available",400);
        }
    }
    public static void validateEmail(User u){
        if(!doesEmailAvailable(u.getEmail())){
            throw new WebException("Email isn't available",400);
        }
    }

    public static void doesEmailAlreadyInUse(User u) throws SQLException {
        if(doesUserWithSuchEmailAlreadyExists(u.getEmail())){
            throw new WebException("This email already in use",400);
        }
    }
    public static void doesLoginAlreadyInUse(User u) throws SQLException {
        if(doesUserWithSuchLoginAlreadyExists(u.getLogin())){
            throw new WebException("User with such login already exist",400);
        }
    }
    public static void doesPhoneAlreadyInUse(User u) throws SQLException {
        if(doesUserWithSuchPhoneAlreadyExists(u.getPhone())){
            throw new WebException("This phone already in use",400);
        }
    }
    public static boolean doesEmailAvailable(String email){
        email = email.trim();
        EmailValidator eValidator = EmailValidator.getInstance();
        if(eValidator.isValid(email)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean doesPhoneAvailable(String swissNumberStr) throws NumberParseException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, null);
        return (phoneUtil.isValidNumber(swissNumberProto));
    }
    public static boolean doesUserWithSuchLoginAlreadyExists(String login) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }
    public static boolean doesUserWithSuchEmailAlreadyExists(String email) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
    public static boolean doesUserWithSuchPhoneAlreadyExists(String phone) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao){
            if(u.getPhone().equals(phone)){
                return true;
            }
        }
        return false;
    }
    public static void checkDoesUserWithSuchIdExists(int userId) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        if(userDao.queryForId(userId)==null){
            throw new WebException("Such user doesn't exist",400);
        }
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
