package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.Subject;
import com.github.bogdan.models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.getIntFieldValue;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchIdExist;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchNameExist;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchIdExists;

public class DeserializerForChangeGroup extends StdDeserializer<Group> {
    public DeserializerForChangeGroup() {
        super(Group.class);
    }

    @Override
    public Group deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
            Dao<Subject,Integer> subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Group group = new Group();

            int groupId = getIntFieldValue(node,"groupId");
            checkDoesGroupWithSuchIdExist(groupId);
            group.setId(groupId);

            Group groupBase = groupDao.queryForId(groupId);

            String date;
            if(node instanceof NullNode){
                group.setDateOfTheCreation(groupBase.getDateOfTheCreation());
            }else if(node.get("date") == null){
                group.setDateOfTheCreation(groupBase.getDateOfTheCreation());
            }else if(node.get("date").asText()==""){
                group.setDateOfTheCreation(groupBase.getDateOfTheCreation());
            } else{
                date = node.get("date").asText();
                checkLocalDateFormat(date);
                group.setDateOfTheCreation(date);
            }

            String name;
            if(node instanceof NullNode){
                group.setGroupName(groupBase.getGroupName());
            }else if(node.get("groupName") == null){
                group.setGroupName(groupBase.getGroupName());
            }else if(node.get("groupName").asText()==""){
                group.setGroupName(groupBase.getGroupName());
            } else{
                name = node.get("groupName").asText();
                if(!name.equals(groupBase.getGroupName())){
                    checkDoesGroupWithSuchNameExist(name);
                }
                group.setGroupName(name);
            }

            int subjectId;
            if(node instanceof NullNode){
                group.setSubject(groupBase.getSubject());
            }else if(node.get("subjectId") == null){
                group.setSubject(groupBase.getSubject());
            }else if(node.get("subjectId").asText()==""){
                group.setSubject(groupBase.getSubject());
            } else{
                subjectId = node.get("subjectId").asInt();
                checkDoesSubjectWithSuchIdExists(subjectId);
                group.setSubject(subjectDao.queryForId(subjectId));
            }
            return group;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
