package fr.unice.polytech.equipe.j.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonConfig {

    // Create an ObjectMapper that can handle Java 8 date/time types
    public static ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());

        // Register custom serializers if needed (for custom types)
        SimpleModule module = new SimpleModule();
        // Example: module.addSerializer(OrderPriceStrategy.class, new CustomSerializer());

        objectMapper.registerModule(module);  // Register any additional modules

        return objectMapper;
    }
}
