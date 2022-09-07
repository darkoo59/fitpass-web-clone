package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Membership;
import model.Training;
import model.TrainingHistory;

import javax.swing.plaf.metal.MetalMenuBarUI;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

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
        ArrayList<Membership> memberships = getAll();
        for (Membership membership:memberships) {
            if(membership.getId().equals(id))
                return membership;
        }
        return null;
    }

    @Override
    public void save(ArrayList<Membership> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }).create();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }).create();
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<Membership>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(Membership obj) throws IOException {
        ArrayList<Membership> memberships = getAll();
        for (Membership membership:memberships) {
            if(membership.getId().equals(obj.getId())) {
                memberships.remove(membership);
                save(memberships);
                return;
            }
        }
    }

    @Override
    public void update(Membership obj) throws IOException {
        ArrayList<Membership> memberships = getAll();
        for (Membership membership:memberships) {
            if(membership.getId().equals(obj.getId())){
                membership.setMembershipId(obj.getMembershipId());
                membership.setCustomerId(obj.getCustomerId());
                membership.setStatus(obj.getStatus());
                membership.setPaymentDate(obj.getPaymentDate());
                membership.setValidityDateTime(obj.getValidityDateTime());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random rd = new Random();
        StringBuilder sb = new StringBuilder();
        do{
            for(int i=0;i<9;i++) {
                char letter = abc.charAt(rd.nextInt(abc.length()));
                sb.append(letter);
            }
        }while(get(sb.toString()) != null);
        return sb.toString();
    }
}
