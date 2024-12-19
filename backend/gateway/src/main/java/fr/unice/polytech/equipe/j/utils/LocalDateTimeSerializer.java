package fr.unice.polytech.equipe.j.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Serialize as an array: [year, month, day, hour, minute, second, nanosecond]
        gen.writeStartArray();
        gen.writeNumber(value.getYear());
        gen.writeNumber(value.getMonthValue()); // Java months are 1-based
        gen.writeNumber(value.getDayOfMonth());
        gen.writeNumber(value.getHour());
        gen.writeNumber(value.getMinute());
        gen.writeNumber(value.getSecond());
        gen.writeNumber(value.getNano());
        gen.writeEndArray();
    }
}
