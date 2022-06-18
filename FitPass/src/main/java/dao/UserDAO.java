package dao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.*;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserDAO implements IDao<User> {

    private final String path = "resources/users.json";
    public UserDAO()
    {

    }

    @Override
    public ArrayList<User> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<User> users = null;
        users = gson.fromJson(reader,new TypeToken<ArrayList<User>>(){}.getType());
        return users;
    }

    @Override
    public User get(String id) throws IOException {
        ArrayList<User> users = getAll();
        for (User user:users) {
            if(user.getId().equals(id))
                return user;
        }
        return null;
    }

    @Override
    public void save(ArrayList<User> objs) throws IOException {
        //TODO
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<ArrayList<User>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void update(User obj) throws IOException {
        ArrayList<User> users = getAll();
        for (User user:users) {
            if(user.getId().equals(obj.getId())){
                user.setBirthDate(obj.getBirthDate());
                user.setGender(obj.getGender());
                user.setName(obj.getName());
                user.setPassword(obj.getPassword());
                user.setRole(obj.getRole());
                user.setSurname(obj.getSurname());
                user.setUsername(obj.getUsername());
                save(users);
                return;
            }
        }
    }

    @Override
    public void delete(User obj) throws IOException {
        ArrayList<User> users = getAll();
        for (User user:users) {
            if(user.getId().equals(obj.getId())) {
                users.remove(user);
                save(users);
                return;
            }
        }
    }
}
