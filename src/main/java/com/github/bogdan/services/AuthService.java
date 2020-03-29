package com.github.bogdan.services;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.github.bogdan.services.ContextService.authorizationFailed;
import static com.github.bogdan.services.UserService.getUserByLogin;

public class AuthService {
    public static boolean authorization(String login, String  password) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);

        for(User u:userDao.queryForAll()){
            if(u.getLogin().equals(login) && BCrypt.checkpw(password,u.getPassword())){
                return true;
            }
        }
        return false;
    }
    public static void checkAuthorization(String login, String password, Context ctx) throws SQLException {
        if(authorization(login,password)){
        }else authorizationFailed(ctx);
    }

}
