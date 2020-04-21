package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.models.Group;
import com.github.bogdan.models.Schedule;
import com.github.bogdan.models.User;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.controllers.ScheduleController.getGroupsSchedule;
import static com.github.bogdan.services.UserGroupService.*;


public class GroupGetSerializer extends StdSerializer<Group> {
    public GroupGetSerializer() {
        super(Group.class);
    }

    @Override
    public void serialize(Group group, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",group.getId());
        jsonGenerator.writeStringField("groupName",group.getGroupName());
        jsonGenerator.writeObjectField("subject",group.getSubject());
        jsonGenerator.writeStringField("dateOfTheCreation",group.getDateOfTheCreation());
        jsonGenerator.writeArrayFieldStart("users");

        try {
            for(User u: getGroupsUsers(group)){
                jsonGenerator.writeObject(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeArrayFieldStart("schedule");
        try {
            for(Schedule schedule:getGroupsSchedule(group.getId())){
                jsonGenerator.writeObject(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
