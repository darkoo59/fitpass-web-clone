package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.TrainingHistory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrainingHistoryDAO implements IDAO<TrainingHistory> {

    private final String path = "resources/trainingHistory.json";
    @Override
    public ArrayList<TrainingHistory> getAll() throws IOException {
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
        ArrayList<TrainingHistory> trainings = new ArrayList<TrainingHistory>();
        trainings = gson.fromJson(reader,new TypeToken<ArrayList<TrainingHistory>>(){}.getType());
        return trainings;
    }

    @Override
    public TrainingHistory get(String id) throws IOException {
        ArrayList<TrainingHistory> trainings = getAll();
        for (TrainingHistory training:trainings) {
            if(training.getId().equals(id))
                return training;
        }
        return null;
    }

    @Override
    public void save(ArrayList<TrainingHistory> objs) throws IOException {
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
        Type listType = new TypeToken<ArrayList<TrainingHistory>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(TrainingHistory obj) throws IOException {
        ArrayList<TrainingHistory> trainings = getAll();
        for (TrainingHistory training:trainings) {
            if(training.getId().equals(obj.getId())) {
                trainings.remove(training);
                save(trainings);
                return;
            }
        }
    }

    @Override
    public void update(TrainingHistory obj) throws IOException {
        ArrayList<TrainingHistory> trainings = getAll();
        for (TrainingHistory training:trainings) {
            if(training.getId().equals(obj.getId())){
                training.setTrainingId(obj.getTrainingId());
                training.setCoachId(obj.getCoachId());
                training.setCustomerId(obj.getCustomerId());
                training.setApplicationDateTime(obj.getApplicationDateTime());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<TrainingHistory> trainings = getAll();
        for (TrainingHistory training:trainings) {
            if(Integer.parseInt(training.getId()) >= id) {
                id = Integer.parseInt(training.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
