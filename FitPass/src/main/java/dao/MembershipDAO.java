package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Membership;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MembershipDAO implements IDao<Membership>{
    private final String path = "resources/membership.json";
    @Override
    public ArrayList<Membership> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
        }).create();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<Membership> memberships = new ArrayList<Membership>();
        memberships = gson.fromJson(reader,new TypeToken<ArrayList<Membership>>(){}.getType());
        return memberships;
    }

    @Override
    public Membership get(String id) throws IOException {
        return null;
    }

    @Override
    public void save(ArrayList<Membership> objs) throws IOException {

    }

    @Override
    public void delete(Membership obj) throws IOException {

    }

    @Override
    public void update(Membership obj) throws IOException {

    }

    @Override
    public String getNewId() throws IOException {
        return null;
    }
}
