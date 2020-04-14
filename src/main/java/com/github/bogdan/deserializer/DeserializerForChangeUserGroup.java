package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.User;
import com.github.bogdan.models.UserGroup;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.bogdan.services.DeserializerService.*;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchIdExist;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.LocalDateService.checkValidDate;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchIdExists;
import static com.github.bogdan.services.UserGroupService.checkDoesSuchRecordExist;
import static com.github.bogdan.services.UserService.checkDoesUserWithSuchIdExists;

import java.io.IOException;
import java.sql.SQLException;


public class DeserializerForChangeUserGroup extends StdDeserializer<UserGroup> {
    public DeserializerForChangeUserGroup() {
        super(UserGroup.class);
    }

    @Override
    public UserGroup deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            Dao<UserGroup, Integer> userGroupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, UserGroup.class);
            UserGroup userGroup = new UserGroup();
            Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);

            int userGroupId = getIntFieldValue(node,"userGroupId");
            checkDoesSuchRecordExist(userGroupId);
            userGroup.setId(userGroupId);

            UserGroup userGroupBase = userGroupDao.queryForId(userGroupId);


            int userId;
            if(node instanceof NullNode){
                userGroup.setUser(userGroupBase.getUser());
            }else if(node.get("userId") == null){
                userGroup.setUser(userGroupBase.getUser());
            }else if(node.get("userId").asText()==""){
                userGroup.setUser(userGroupBase.getUser());
            } else{
                userId = node.get("userId").asInt();
                checkDoesUserWithSuchIdExists(userId);
                userGroup.setUser(userDao.queryForId(userId));
            }

            int groupId;
            if(node instanceof NullNode){
                userGroup.setGroup(userGroupBase.getGroup());
            }else if(node.get("groupId") == null){
                userGroup.setGroup(userGroupBase.getGroup());
            }else if(node.get("groupId").asText()==""){
                userGroup.setGroup(userGroupBase.getGroup());
            } else{
                groupId = node.get("groupId").asInt();
                checkDoesGroupWithSuchIdExist(groupId);
                userGroup.setGroup(groupDao.queryForId(groupId));
            }

            String dateOfEnrollment;
            if(node instanceof NullNode){
                userGroup.setDateOfEnrollment(userGroupBase.getDateOfEnrollment());
            }else if(node.get("dateOfEnrollment") == null){
                userGroup.setDateOfEnrollment(userGroupBase.getDateOfEnrollment());
            }else if(node.get("dateOfEnrollment").asText()==""){
                userGroup.setDateOfEnrollment(userGroupBase.getDateOfEnrollment());
            } else{
                dateOfEnrollment = node.get("dateOfEnrollment").asText();
                checkLocalDateFormat(dateOfEnrollment);
                userGroup.setDateOfEnrollment(dateOfEnrollment);
            }

            String dateOfDrop;
            if(node instanceof NullNode){
                userGroup.setDateOfDrop(userGroupBase.getDateOfDrop());
            }else if(node.get("dateOfDrop") == null){
                userGroup.setDateOfDrop(userGroupBase.getDateOfDrop());
            }else if(node.get("dateOfDrop").asText()==""){
                userGroup.setDateOfDrop(userGroupBase.getDateOfDrop());
            } else{
                dateOfDrop = node.get("dateOfDrop").asText();
                checkLocalDateFormat(dateOfDrop);
                userGroup.setDateOfDrop(dateOfDrop);
            }
            if(userGroup.getDateOfDrop()!=null) {
                checkValidDate(userGroup.getDateOfEnrollment(), userGroup.getDateOfDrop());
            }
            return userGroup;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
