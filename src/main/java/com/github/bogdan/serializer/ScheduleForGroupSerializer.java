package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.models.Schedule;

import java.io.IOException;

public class ScheduleForGroupSerializer extends StdSerializer<Schedule> {

    public ScheduleForGroupSerializer() {
        super(Schedule.class);
    }

    @Override
    public void serialize(Schedule schedule, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("startOfTheLesson",schedule.getStartOfTheLesson());
        jsonGenerator.writeStringField("endOfTheLesson",schedule.getEndOfTheLesson());
        jsonGenerator.writeStringField("day",schedule.getDay().toString());
        jsonGenerator.writeNumberField("cabinet",schedule.getCabinet());
        jsonGenerator.writeEndObject();
    }
}
