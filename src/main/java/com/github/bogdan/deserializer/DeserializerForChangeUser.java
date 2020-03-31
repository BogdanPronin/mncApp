package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.MyException;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Role;
import com.github.bogdan.modals.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.*;

public class DeserializerForChangeUser  extends StdDeserializer<User> {
    static final Logger LOGGER = LoggerFactory.getLogger(DeserializerForChangeUser.class);
    public DeserializerForChangeUser() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException{
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
            int id;

            User u = new User();
            checkForExplicitlyNullField(node.get("id"),"Necessary field id can't be null");
            if(node.get("id")==null){
                throw new MyException("Write necessary field id");
            }else id = node.get("id").asInt();
            if(userDao.queryForId(id)==null){
                throw new WebException("User with such id doesn't exist",400);
            }
            u.setId(id);
            checkForExplicitlyNullField(node.get("fname"),"Necessary field fname can't be null");
            if(node.get("fname") ==null){
                u.setFname(userDao.queryForId(id).getFname());
            }else u.setFname( node.get("fname").asText());

            checkForExplicitlyNullField(node.get("lname"),"Necessary field lname can't be null");
            if(node.get("lname") ==null){
                u.setLname(userDao.queryForId(id).getLname());
            }else u.setLname(node.get("lname").asText());

            checkForExplicitlyNullField(node.get("phone"),"Necessary field phone can't be null");
            if(node.get("phone")==null){
                LOGGER.info(userDao.queryForId(id).getPhone());
                u.setPhone(userDao.queryForId(id).getPhone());
            }else u.setPhone(node.get("phone").asText());

            checkForExplicitlyNullField(node.get("email"),"Necessary field email can't be null");
            if(node.get("email")==null){
                u.setEmail(userDao.queryForId(id).getEmail());
            }else u.setEmail(node.get("email").asText());

            checkForExplicitlyNullField(node.get("login"),"Necessary field login can't be null");
            if(node.get("login")==null){
                u.setLogin(userDao.queryForId(id).getLogin());
            }else u.setLogin(node.get("login").asText());

            checkForExplicitlyNullField(node.get("password"),"Necessary field password can't be null");
            if(node.get("password")==null){
                u.setPassword(userDao.queryForId(id).getPassword());
            }else {
                u.setPassword(node.get("password").asText());
                String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12));
                u.setPassword(hashedPassword);
            }

            checkForExplicitlyNullField(node.get("role"),"Necessary field role can't be null");
            if(node.get("role")==null){
                u.setRole(userDao.queryForId(id).getRole());
            }else u.setRole(Role.valueOf(node.get("role").asText()));
            u.setDateOfRegister(userDao.queryForId(id).getDateOfRegister());
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }

    }


}
