package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.modals.Days;
import com.github.bogdan.modals.Group;
import com.github.bogdan.modals.Schedule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.services.DeserializerService.getIntFieldValue;
import static com.github.bogdan.services.GroupService.checkDoesGroupWithSuchIdExist;
import static com.github.bogdan.services.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.services.LocalDateService.checkLocalDateTimeFormat;
import static com.github.bogdan.services.ScheduleService.checkDoesScheduleWithSuchIdExist;

public class DeserializerForChangeSchedule extends StdDeserializer<Schedule> {
    public DeserializerForChangeSchedule() {
        super(Schedule.class);
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<Schedule,Integer> scheduleDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Schedule.class);
            Dao<Group,Integer> groupDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Group.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Schedule schedule = new Schedule();

            int scheduleId = getIntFieldValue(node,"scheduleId");
            checkDoesScheduleWithSuchIdExist(scheduleId);
            schedule.setId(scheduleId);

            Schedule scheduleBase = scheduleDao.queryForId(scheduleId);

            String startOfTheLesson;
            if(node instanceof NullNode){
                schedule.setStartOfTheLesson(scheduleBase.getStartOfTheLesson());
            }else if(node.get("startOfTheLesson") == null){
                schedule.setStartOfTheLesson(scheduleBase.getStartOfTheLesson());
            }else if(node.get("startOfTheLesson").asText()==""){
                schedule.setStartOfTheLesson(scheduleBase.getStartOfTheLesson());
            } else{
                startOfTheLesson = node.get("startOfTheLesson").asText();
                checkLocalDateTimeFormat(startOfTheLesson);
                schedule.setStartOfTheLesson(startOfTheLesson);
            }

            String endOfTheLesson;
            if(node instanceof NullNode){
                schedule.setEndOfTheLesson(scheduleBase.getEndOfTheLesson());
            }else if(node.get("endOfTheLesson") == null){
                schedule.setEndOfTheLesson(scheduleBase.getEndOfTheLesson());
            }else if(node.get("endOfTheLesson").asText()==""){
                schedule.setEndOfTheLesson(scheduleBase.getEndOfTheLesson());
            } else{
                endOfTheLesson = node.get("endOfTheLesson").asText();
                checkLocalDateTimeFormat(endOfTheLesson);
                schedule.setEndOfTheLesson(endOfTheLesson);
            }

            String day;
            if(node instanceof NullNode){
                schedule.setDay(scheduleBase.getDay());
            }else if(node.get("day") == null){
                schedule.setDay(scheduleBase.getDay());
            }else if(node.get("day").asText()==""){
                schedule.setDay(scheduleBase.getDay());
            } else{
                day = node.get("day").asText();
                schedule.setDay(Days.valueOf(day));
            }

            int groupId;
            if(node instanceof NullNode){
                schedule.setGroup(scheduleBase.getGroup());
            }else if(node.get("groupId") == null){
                schedule.setGroup(scheduleBase.getGroup());
            }else if(node.get("groupId").asText()==""){
                schedule.setGroup(scheduleBase.getGroup());
            } else{
                groupId = node.get("groupId").asInt();
                checkDoesGroupWithSuchIdExist(groupId);
                schedule.setGroup(groupDao.queryForId(groupId));
            }

            int cabinet;
            if(node instanceof NullNode){
                schedule.setCabinet(scheduleBase.getCabinet());
            }else if(node.get("cabinet") == null){
                schedule.setCabinet(scheduleBase.getCabinet());
            }else if(node.get("cabinet").asText()==""){
                schedule.setCabinet(scheduleBase.getCabinet());
            } else{
                cabinet = node.get("cabinet").asInt();
                schedule.setCabinet(cabinet);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
