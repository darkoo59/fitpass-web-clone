package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Content;
import model.User;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class ContentDAO implements IDAO<Content>{

    private final String path = "resources/content.json";
    @Override
    public ArrayList<Content> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<Content> contents = null;
        contents = gson.fromJson(reader,new TypeToken<ArrayList<Content>>(){}.getType());
        return contents;
    }

    @Override
    public Content get(String id) throws IOException {
        ArrayList<Content> contents = getAll();
        for (Content content:contents) {
            if(content.getId().equals(id))
                return content;
        }
        return null;
    }

    @Override
    public void save(ArrayList<Content> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<Content>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(Content obj) throws IOException {
        ArrayList<Content> contents = getAll();
        for (Content content:contents) {
            if(content.getId().equals(obj.getId())) {
                contents.remove(content);
                save(contents);
                return;
            }
        }
    }

    @Override
    public void update(Content obj) throws IOException {
        ArrayList<Content> contents = getAll();
        for (Content content:contents) {
            if(content.getId().equals(obj.getId())){
               content.setDescription(obj.getDescription());
               content.setName(obj.getName());
               content.setType(obj.getType());
               content.setFacilityId(obj.getFacilityId());
               content.setImage(obj.getImage());
               content.setDuration(obj.getDuration());
               save(contents);
               return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 1;
        ArrayList<Content> contents = getAll();
        for (Content content:contents) {
            if(Integer.parseInt(content.getId()) >= id) {
                id = Integer.parseInt(content.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
