package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.AdministratorController;
import dao.IDao;
import dao.SportsFacilityDAO;
import dao.UserDAO;
import model.Location;
import model.SportsFacility;
import model.User;
import spark.*;
import utils.enums.GenderType;
import utils.enums.RoleType;
import utils.enums.SportsFacilityStatus;
import utils.others.Address;
import utils.others.WorkHour;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import javax.validation.Payload;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import static main.main.gson;

public class AdministratorService {
    private IDao userDAO;
    private SportsFacilityDAO facilityDAO;
    private ArrayList<User> allUsers;
    public AdministratorService() throws IOException {
        userDAO = new UserDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
    }
    public void register(Request req) throws IOException {
        String name = req.queryParams("name");
        String surname = req.queryParams("surname");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String date = req.queryParams("date");
        LocalDate parsedDate = LocalDate.parse(date);
        String sex = req.queryParams("sexRadioOptions");
        GenderType sexType = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        String role = req.queryParams("roleRadioOptions");
        RoleType roleType = role.equals("manager") ? RoleType.MANAGER : RoleType.COACH;
        User newUser = new User(username, password, name, surname, sexType, parsedDate, roleType);
        newUser.setId(userDAO.getNewId());
        ArrayList<User> users = userDAO.getAll();
        users.add(newUser);
        userDAO.save(users);
    }

    public ArrayList<User> getSearchedProfiles(Request req) throws IOException {
        ArrayList<User> searchedUsers = new ArrayList<User>();
        for(User user : allUsers)
        {
            if(user.getName().contains(req.body()) || user.getSurname().contains(req.body()) || user.getUsername().contains(req.body()))
                searchedUsers.add(user);
        }
        for(User user : searchedUsers)
            System.out.println(user.getUsername());
        return searchedUsers;
    }

    public ArrayList<User> getAllProfiles() throws IOException {
        return userDAO.getAll();
    }

    public ArrayList<User> getManagersForNewFacility() throws IOException {
        ArrayList<User> managers = new ArrayList<User>();
        ArrayList<SportsFacility> allFacilities = facilityDAO.getAll();
        ArrayList<User> allUsers = userDAO.getAll();
        ArrayList<String> managersWithFacilityId = new ArrayList<String>();
        for(SportsFacility facility : allFacilities)
            managersWithFacilityId.add(facility.getManagerId());
        for(User user:allUsers)
        {
            if(user.getRole() == RoleType.MANAGER && !managersWithFacilityId.contains(user.getId()))
            {
                managers.add(user);
            }
        }
        return managers;
    }

    public void createNewFacility(Request req) throws Exception {
        ArrayList<String> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
        Address address = new Address(
                payload.get(3),
                payload.get(4),
                payload.get(5),
                payload.get(6)
        );
        Location location = new Location(
                Double.parseDouble(payload.get(7)),
                Double.parseDouble(payload.get(8)),
                address
        );
        LocalTime time = LocalTime.of(0, 0);
        WorkHour workHour = new WorkHour(time ,time);
        SportsFacility facility = new SportsFacility(
                payload.get(0),                 //name
                payload.get(1),                 //type
                null,                           //content
                SportsFacilityStatus.WORKING,   //status
                location,                       //location
                payload.get(9),                 //logo
                0.0,                            //averageRating
                workHour,                       //workHour
                payload.get(2)                  //managerID
        );
        ArrayList<SportsFacility> facilities = facilityDAO.getAll();
        facility.setId(facilityDAO.getNewId());
        facilities.add(facility);
        facilityDAO.save(facilities);
    }

    public String createNewFacilityLogo(Request req, String name) throws Exception {
        String location = "";
        long maxFileSize = 100000000;
        long maxRequestSize = 100000000;
        int fileSizeThreshold = 1024;

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                location, maxFileSize, maxRequestSize, fileSizeThreshold);
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                multipartConfigElement);

        Part photo = req.raw().getPart("photo");
        String photoName = photo.getSubmittedFileName();
        String ext = photoName.substring(photoName.lastIndexOf(".") + 1);

        Path out = Paths.get("src/main/resources/static/vue/src/assets/images/" + name + "." + ext);
        try (final InputStream in = photo.getInputStream()) {
            Files.copy(in, out);
            photo.delete();
        }
        multipartConfigElement = null;
        photo = null;
        return name + "." + ext;
    }
}
