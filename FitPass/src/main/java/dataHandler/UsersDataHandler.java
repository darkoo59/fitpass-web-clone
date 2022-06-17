package dataHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.User;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;

import java.io.File;
import java.time.LocalDate;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersDataHandler {
    private final String path = "resources/users.json";

    public ArrayList<User> loadDataFromFile() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<User> users = null;
        users = gson.fromJson(reader,new TypeToken<ArrayList<User>>(){}.getType());
        return users;
    }
}
