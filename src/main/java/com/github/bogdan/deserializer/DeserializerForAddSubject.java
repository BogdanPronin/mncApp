package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.models.Subject;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.getStringFieldValue;
import static com.github.bogdan.services.SubjectService.checkDoesSubjectWithSuchNameExist;

public class DeserializerForAddSubject extends StdDeserializer<Subject> {
    protected DeserializerForAddSubject() {
        super(Subject.class);
    }

    @Override
    public Subject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name = getStringFieldValue(node,"subjectName");
        try {
            checkDoesSubjectWithSuchNameExist(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Subject(name);
    }
}
