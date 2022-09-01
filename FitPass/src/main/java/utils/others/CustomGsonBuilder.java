package utils.others;

import java.time.LocalDate;
import java.time.LocalTime;

import com.google.gson.GsonBuilder;

public class CustomGsonBuilder {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    public GsonBuilder getCustomGsonBuilder() {
        gsonBuilder.registerTypeAdapter(LocalDate .class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime .class, new LocalTimeConverter());
        gsonBuilder.setPrettyPrinting().create();
        return gsonBuilder;
    }
}
