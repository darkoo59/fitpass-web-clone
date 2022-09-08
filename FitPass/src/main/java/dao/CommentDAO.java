package dao;

import com.google.gson.reflect.TypeToken;
import model.Comment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static main.main.gson;

public class CommentDAO implements IDAO<Comment> {
    private final String path = "resources/comments.json";
    @Override
    public ArrayList<Comment> getAll() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        ArrayList<Comment> comment = null;
        comment = gson.fromJson(reader, new TypeToken<ArrayList<Comment>>(){}.getType());
        return comment;
    }

    @Override
    public Comment get(String id) throws IOException {
        ArrayList<Comment> comments = getAll();
        for (Comment comment : comments) {
            if (comment.getId().equals(id))
                return comment;
        }
        return null;
    }

    @Override
    public void save(ArrayList<Comment> objs) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Type listType = new TypeToken<ArrayList<Comment>>(){}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(Comment obj) throws IOException {
        ArrayList<Comment> comments = getAll();
        for (Comment comment : comments) {
            if (comment.getId().equals(obj.getId())) {
                comments.remove(comment);
                save(comments);
                return;
            }
        }
    }

    @Override
    public void update(Comment obj) throws IOException {
        ArrayList<Comment> comments = getAll();
        for (Comment comment : comments) {
            if (comment.getId().equals(obj.getId())){
                comment.setStatus(obj.getStatus());
                comment.setRating(obj.getRating());
                comment.setText(obj.getText());
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<Comment> comments = getAll();
        for (Comment comment : comments) {
            if (Integer.parseInt(comment.getId()) >= id) {
                id = Integer.parseInt(comment.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
