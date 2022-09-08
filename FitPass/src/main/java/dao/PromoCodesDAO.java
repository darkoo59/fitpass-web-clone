package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Membership;
import model.PromoCodes;
import model.SportsFacility;

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

public class PromoCodesDAO implements IDAO<PromoCodes>{

    private final String path = "resources/promoCodes.json";
    @Override
    public ArrayList<PromoCodes> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
        }).create();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<PromoCodes> codes = new ArrayList<PromoCodes>();
        codes = gson.fromJson(reader,new TypeToken<ArrayList<PromoCodes>>(){}.getType());
        return codes;
    }

    @Override
    public PromoCodes get(String id) throws IOException {
        ArrayList<PromoCodes> codes = getAll();
        for (PromoCodes code:codes) {
            if(code.getId().equals(id))
                return code;
        }
        return null;
    }

    @Override
    public void save(ArrayList<PromoCodes> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }).create();
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<PromoCodes>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(PromoCodes obj) throws IOException {
        ArrayList<PromoCodes> codes = getAll();
        for (PromoCodes code:codes) {
            if(code.getId().equals(obj.getId())) {
                codes.remove(code);
                save(codes);
                return;
            }
        }
    }

    @Override
    public void update(PromoCodes obj) throws IOException {
        ArrayList<PromoCodes> codes = getAll();
        for (PromoCodes code:codes) {
            if(code.getId().equals(obj.getId())){
                code.setCode(obj.getCode());
                code.setMaxUsage(obj.getMaxUsage());
                code.setValidUntil(obj.getValidUntil());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<PromoCodes> codes = getAll();
        for (PromoCodes code:codes) {
            if(Integer.parseInt(code.getId()) >= id) {
                id = Integer.parseInt(code.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
