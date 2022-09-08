package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.SportsFacility;
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

public class SportsFacilityDAO implements IDAO<SportsFacility> {
    private final String path = "resources/sportsFacilities.json";
    @Override
    public ArrayList<SportsFacility> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeConverter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        ArrayList<SportsFacility> sportsFacilities = null;
        sportsFacilities = gson.fromJson(reader,new TypeToken<ArrayList<SportsFacility>>(){}.getType());
        return sportsFacilities;
    }

    @Override
    public SportsFacility get(String id) throws IOException {
        ArrayList<SportsFacility> sportsFacilities = getAll();
        for (SportsFacility facility:sportsFacilities) {
            if(facility.getId().equals(id))
                return facility;
        }
        return null;
    }

    @Override
    public void save(ArrayList<SportsFacility> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeConverter());
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<SportsFacility>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(SportsFacility obj) throws IOException {
        ArrayList<SportsFacility> sportsFacilities = getAll();
        for (SportsFacility sportsFacility:sportsFacilities) {
            if(sportsFacility.getId().equals(obj.getId())) {
                sportsFacilities.remove(sportsFacility);
                save(sportsFacilities);
                return;
            }
        }
    }

    @Override
    public void update(SportsFacility obj) throws IOException {
        ArrayList<SportsFacility> sportsFacilities = getAll();
        for (SportsFacility facility:sportsFacilities) {
            if(facility.getId().equals(obj.getId())){
                facility.setName(obj.getName());
                facility.setAverageRating(obj.getAverageRating());
                facility.setContent(obj.getContent());
                facility.setLocation(obj.getLocation());
                facility.setLogo(obj.getLogo());
                facility.setStatus(obj.getStatus());
                facility.setType(obj.getType());
                facility.setWorkHour(obj.getWorkHour());
                facility.setManagerId(obj.getManagerId());
                return;
            }
        }
    }

    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<SportsFacility> facilities = getAll();
        for (SportsFacility facility:facilities) {
            if(Integer.parseInt(facility.getId()) >= id) {
                id = Integer.parseInt(facility.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }

    public ArrayList<SportsFacility> getFacilitiesByManagerId(String id) throws IOException {
        ArrayList<SportsFacility> facilities = getAll();
        ArrayList<SportsFacility> managerFacilities = new ArrayList<SportsFacility>();
        for (SportsFacility facility:facilities) {
            if(facility.getManagerId().equals(id)) {
                managerFacilities.add(facility);
            }
        }
        return managerFacilities;
    }
}
