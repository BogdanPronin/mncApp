package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.MyException;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Subject;
import com.github.bogdan.modals.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.LogManager;

public class DeserializerForAddGroup extends StdDeserializer {

    public DeserializerForAddGroup() {
        super(Group.class);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Dao<Subject,Integer> subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
            String name;
            String date = "";
            int subjectId;
            checkForExplicitlyNullField(node.get("groupName"),"Necessary field \"groupName\" can't be null");
            if(node.get("groupName") == null){
                throw new WebException("Necessary field \"groupName\" can't be null",400);
            }else name = node.get("groupName").asText();

            checkForExplicitlyNullField(node.get("subjectId"),"Necessary field \"subjectId\" can't be null");
            if(node.get("subjectId")==null){
                throw new WebException("Necessary field \"subjectId\" can't be null",400);
            }
            subjectId = node.get("subjectId").asInt();
            if (subjectDao.queryForId(subjectId) ==null){
                throw new WebException("Subject isn't exist",400);
            }

            if (node.get("dateOfCreation") == null) {
                LocalDate localDate = LocalDate.now();
                date = localDate.toString();
            }else if(node.get("dateOfCreation").asText() == null){
                LocalDate localDate = LocalDate.now();
                date = localDate.toString();
            }else date = node.get("dateOfCreation").asText();
            Group g = new Group(name,subjectDao.queryForId(subjectId),date);
            return g;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void checkForExplicitlyNullField(JsonNode node, String exceptionMessage){

        if (node instanceof NullNode) {
            throw new WebException(exceptionMessage,400);
        }
    }

}
