package fr.unice.polytech.equipe.j.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        // Check if the node is an array or a string
        if (node.isArray()) {
            // Handle the array format: [year, month, day, hour, minute, second, nanosecond]
            int year = node.get(0).asInt();
            int month = node.get(1).asInt();
            int day = node.get(2).asInt();
            int hour = node.get(3).asInt();
            int minute = node.get(4).asInt();
            int second = node.get(5).asInt();
            int nano = node.get(6).asInt();

            return LocalDateTime.of(year, month, day, hour, minute, second, nano);
        } else if (node.isTextual()) {
            // Handle the ISO-8601 string format
            String dateString = node.asText();
            Instant instant = Instant.parse(dateString);
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            throw new JsonParseException(p, "Unexpected value for deliveryTime. Expected either an array or a string.");
        }
    }
}
