package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.*;

public class DeserializerForAddGroup extends StdDeserializer {

    public DeserializerForAddGroup() {
        super(Group.class);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            String name = getStringFieldValue(node,"groupName");

            Dao<Subject,Integer> subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
            int subjectId = getIntFieldValue(node,"subjectId");
            if (subjectDao.queryForId(subjectId) ==null){
                throw new WebException("Subject isn't exist",400);
            }

            String date = getDateFieldValue(node,"dateOfCreation");

            Group g = new Group(name,subjectDao.queryForId(subjectId),date);
            return g;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
