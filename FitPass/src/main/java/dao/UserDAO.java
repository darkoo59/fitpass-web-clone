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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import static main.main.gson;

public class UserDAO implements IUserDAO {

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
        ArrayList<User> users = gson.fromJson(reader,new TypeToken<ArrayList<User>>(){}.getType());
        ArrayList<User> nonDeletedusers = new ArrayList<>();
        for (User user : users) {
            if (!user.isDeleted()) {
                nonDeletedusers.add(user);
            }
        }
        return nonDeletedusers;
    }

    @Override
    public ArrayList<User> getAllAndDeleted() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        return gson.fromJson(reader, new TypeToken<ArrayList<User>>(){}.getType());
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
        Gson gson = gsonBuilder.setPrettyPrinting().create();
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

    public void updateUserInfo(User obj) throws IOException {
        ArrayList<User> users = getAll();
        for (User user : users) {
            if (user.getId().equals(obj.getId())) {
                user.setUsername(obj.getUsername());
                user.setName(obj.getName());
                user.setSurname(obj.getSurname());
                user.setGender(obj.getGender());
                user.setBirthDate(obj.getBirthDate());
                save(users);
                return;
            }
        }
    }

    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<User> users = getAll();
        for (User user:users) {
            if(Integer.parseInt(user.getId()) >= id) {
                id = Integer.parseInt(user.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}