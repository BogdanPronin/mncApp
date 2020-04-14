package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.models.Subject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.getIntFieldValue;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchNameExist;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchIdExists;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchNameExist;

public class DeserializerForChangeSubject extends StdDeserializer<Subject> {

    public DeserializerForChangeSubject() {
        super(Subject.class);
    }

    @Override
    public Subject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<Subject,Integer> subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Subject subject = new Subject();
            int subjectId = getIntFieldValue(node,"subjectId");
            checkDoesSubjectWithSuchIdExists(subjectId);
            subject.setId(subjectId);
            Subject subjectBase = subjectDao.queryForId(subjectId);

            String name = null;
            if(node instanceof NullNode){
                subject.setName(subjectBase.getName());
            }else if(node.get("subjectName") == null){
                subject.setName(subjectBase.getName());
            }else if(node.get("subjectName").asText()==""){
                subject.setName(subjectBase.getName());
            } else{
                name = node.get("subjectName").asText();
                if(!name.equals(subjectBase.getName())){
                    checkDoesSubjectWithSuchNameExist(name);
                }
                subject.setName(name);
            }
            return subject;
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return null;
    }
}
