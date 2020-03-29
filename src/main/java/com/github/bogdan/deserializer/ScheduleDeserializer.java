package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exceptions.WebException;
import com.github.bogdan.modals.Days;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.getFieldValue;
import static com.github.bogdan.services.DeserializerService.getIntFieldValue;

public class ScheduleDeserializer  extends StdDeserializer<Schedule> {
    public ScheduleDeserializer() {
        super(Schedule.class);
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String startOfTheLesson = getFieldValue(node,"startOfTheLesson");
            String endOfTheLesson = getFieldValue(node,"endOfTheLesson");
            String day = getFieldValue(node,"day");
            int cabinet = getIntFieldValue(node,"cabinet");
            int groupId = getIntFieldValue(node,"groupId");
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
            if(groupDao.queryForId(groupId)==null){
                throw new WebException("Such group isn't exist",400);
            }
            return new Schedule(startOfTheLesson,endOfTheLesson,Days.valueOf(day),cabinet,groupDao.queryForId(groupId));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

}
