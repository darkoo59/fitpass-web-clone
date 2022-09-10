package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Customer;
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

public class CustomerDAO implements IDAO<Customer>{
    private final String path = "resources/customer.json";

    @Override
    public ArrayList<Customer> getAll() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        ArrayList<Customer> customers = null;
        customers = gson.fromJson(reader,new TypeToken<ArrayList<Customer>>(){}.getType());
        return customers;
    }

    @Override
    public Customer get(String id) throws IOException {
        ArrayList<Customer> customers = getAll();
        for (Customer customer:customers) {
            if(customer.getId().equals(id))
                return customer;
        }
        return null;
    }

    @Override
    public void save(ArrayList<Customer> objs) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<Customer>>() {}.getType();
        String str = gson.toJson(objs, listType);
        writer.write(str);
        writer.close();
    }

    @Override
    public void delete(Customer obj) throws IOException {
        ArrayList<Customer> customers = getAll();
        for (Customer customer:customers) {
            if(customer.getId().equals(obj.getId())) {
                customers.remove(customer);
                save(customers);
                return;
            }
        }
    }

    @Override
    public void update(Customer obj) throws IOException {
        ArrayList<Customer> customers = getAll();
        for (Customer customer:customers) {
            if(customer.getId().equals(obj.getId())){
                customer.setBirthDate(obj.getBirthDate());
                customer.setGender(obj.getGender());
                customer.setName(obj.getName());
                customer.setPassword(obj.getPassword());
                customer.setRole(obj.getRole());
                customer.setSurname(obj.getSurname());
                customer.setUsername(obj.getUsername());
                customer.setCollectedPoints(obj.getCollectedPoints());
                customer.setMembership(obj.getMembership());
                customer.setVisitedFacilities(obj.getVisitedFacilities());
                customer.setType(obj.getType());
                save(customers);
                return;
            }
        }
    }

    @Override
    public String getNewId() throws IOException {
        return null;
    }
}
