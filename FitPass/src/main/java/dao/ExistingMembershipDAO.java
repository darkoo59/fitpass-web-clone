package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.ExistingMembership;
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

public class ExistingMembershipDAO implements IDAO<ExistingMembership> {

    private final String path = "resources/existingMembership.json";
    @Override
    public ArrayList<ExistingMembership> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        ArrayList<ExistingMembership> memberships = null;
        memberships = gson.fromJson(reader,new TypeToken<ArrayList<ExistingMembership>>(){}.getType());
        return memberships;
    }

    @Override
    public ExistingMembership get(String id) throws IOException {
        ArrayList<ExistingMembership> memberships = getAll();
        for (ExistingMembership membership:memberships) {
            if(membership.getId().equals(id))
                return membership;
        }
        return null;
    }

    @Override
    public void save(ArrayList<ExistingMembership> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<ArrayList<ExistingMembership>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(ExistingMembership obj) throws IOException {
        ArrayList<ExistingMembership> memberships = getAll();
        for (ExistingMembership membership:memberships) {
            if(membership.getId().equals(obj.getId())) {
                memberships.remove(membership);
                save(memberships);
                return;
            }
        }
    }

    @Override
    public void update(ExistingMembership obj) throws IOException {
        ArrayList<ExistingMembership> memberships = getAll();
        for (ExistingMembership membership:memberships) {
            if(membership.getId().equals(obj.getId())){
                membership.setMembershipType(obj.getMembershipType());
                membership.setDailyTerms(obj.getDailyTerms());
                membership.setPrice(obj.getPrice());
                membership.setName(obj.getName());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<ExistingMembership> memberships = getAll();
        for (ExistingMembership membership:memberships) {
            if(Integer.parseInt(membership.getId()) >= id) {
                id = Integer.parseInt(membership.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
