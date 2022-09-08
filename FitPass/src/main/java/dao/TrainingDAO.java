package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Training;
import utils.others.LocalTimeConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;

public class TrainingDAO implements IDAO<Training> {

    private final String path = "resources/training.json";
    @Override
    public ArrayList<Training> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeConverter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        ArrayList<Training> trainings = null;
        trainings = gson.fromJson(reader,new TypeToken<ArrayList<Training>>(){}.getType());
        return trainings;
    }

    @Override
    public Training get(String id) throws IOException {
        ArrayList<Training> trainings = getAll();
        for (Training training:trainings) {
            if(training.getId().equals(id))
                return training;
        }
        return null;
    }

    @Override
    public void save(ArrayList<Training> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeConverter());
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<ArrayList<Training>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(Training obj) throws IOException {
        ArrayList<Training> trainings = getAll();
        for (Training training:trainings) {
            if(training.getId().equals(obj.getId())) {
                trainings.remove(training);
                save(trainings);
                return;
            }
        }
    }

    @Override
    public void update(Training obj) throws IOException {
        ArrayList<Training> trainings = getAll();
        for (Training training:trainings) {
            if(training.getId().equals(obj.getId())){
                training.setName(obj.getName());
                training.setType(obj.getType());
                training.setFacilityId(obj.getFacilityId());
                training.setDuration(obj.getDuration());
                training.setCoachId(obj.getCoachId());
                training.setDescription(obj.getDescription());
                training.setImage(obj.getImage());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<Training> trainings = getAll();
        for (Training training:trainings) {
            if(Integer.parseInt(training.getId()) >= id) {
                id = Integer.parseInt(training.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
