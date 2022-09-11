package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Content;
import model.Customer;
import model.CustomerType;
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

public class CustomerTypeDAO implements IDAO<CustomerType> {
    private final String path = "resources/customerTypes.json";

    @Override
    public ArrayList<CustomerType> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<CustomerType> customerTypes = gson.fromJson(reader,new TypeToken<ArrayList<CustomerType>>(){}.getType());
        return customerTypes;
    }

    @Override
    public CustomerType get(String id) throws IOException {
        ArrayList<CustomerType> customerTypes = getAll();
        for (CustomerType customerType : customerTypes) {
            if(customerType.getId().equals(id))
                return customerType;
        }
        return null;
    }

    @Override
    public void save(ArrayList<CustomerType> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<CustomerType>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(CustomerType obj) throws IOException {
        ArrayList<CustomerType> customerTypes = getAll();
        for (CustomerType customerType : customerTypes) {
            if(customerType.getId().equals(obj.getId())) {
                customerTypes.remove(customerType);
                save(customerTypes);
                return;
            }
        }
    }

    @Override
    public void update(CustomerType obj) throws IOException {
        ArrayList<CustomerType> customerTypes = getAll();
        for (CustomerType customerType : customerTypes) {
            if(customerType.getId().equals(obj.getId())){
                customerType.setPoints(obj.getPoints());
                customerType.setPercentage(obj.getPercentage());
                customerType.setType(obj.getType());
                save(customerTypes);
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        int id = 0;
        ArrayList<CustomerType> customerTypes = getAll();
        for (CustomerType customerType : customerTypes) {
            if(Integer.parseInt(customerType.getId()) >= id) {
                id = Integer.parseInt(customerType.getId()) + 1;
            }
        }
        return String.valueOf(id);
    }
}
