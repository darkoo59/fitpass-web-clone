package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.nimbusds.jwt.SignedJWT;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.IUserDAO;
import dao.UserDAO;
import dto.UserDTO;
import io.jsonwebtoken.*;
import model.Credentials;
import model.Customer;
import model.User;
import model.*;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import utils.enums.GenderType;
import utils.enums.RoleType;
import utils.others.Filter;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import javax.xml.bind.DatatypeConverter;

import static java.util.Comparator.comparing;
import static main.main.gson;

public class AccountService {
    private IUserDAO userDAO;
    private CustomerDAO customerDAO;
    private UserService userService;
    private static String key = "fhjdfddjsaildhadlHHHjjFHaFHAkflfhAFKLJhfk";
    private  static String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
    private static byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key);
    private ObjectMapper mapper;

    public AccountService() {
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        userService = new UserService();
        mapper = new ObjectMapper();
    }

    public String register(Request req) throws IOException {
        ArrayList<String> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
        String name = payload.get(0);
        String surname = payload.get(1);
        String username = payload.get(2);
        for (User user : userDAO.getAll()) {
            if (username.equals(user.getUsername())) {
                return "USER_EXISTS";
            }
        }
        String password = payload.get(3);
        String date = payload.get(4);
        LocalDate parsedDate = LocalDate.parse(date);
        String sex = payload.get(5);
        GenderType type = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        User newUser = new User(username, password, name, surname, type, parsedDate, RoleType.CUSTOMER);
        ArrayList<User> users = userDAO.getAllAndDeleted();
        Customer customer = new Customer(
                newUser,
                "",
                new ArrayList<>(),
                0.0,
                new CustomerTypeDAO().get("1")
        );
        ArrayList<Customer> customers = customerDAO.getAll();
        String id = userDAO.getNewId();
        customer.setId(id);
        customers.add(customer);
        customerDAO.save(customers);
        newUser.setId(id);
        users.add(newUser);
        userDAO.save(users);
        return "SUCCESS";
    }

    public String loginUser(Request request) throws IOException {
        Credentials cred;
        cred = mapper.readValue(request.body(),Credentials.class);
        ArrayList<User> users = userDAO.getAll();
        for (User user:users) {
            if(user.getUsername().equals(cred.getUsername()) && user.getPassword().equals(cred.getPassword()))
            {
                String jws =
                        Jwts.builder().setSubject(user.getUsername()).
                                setExpiration(new Date(new Date().getTime() +
                                        1000*10L)).
                                setIssuedAt(new Date()).
                                signWith(SignatureAlgorithm.HS256,secretBytes).
                                compact();

                user.setJWT(jws);
                userDAO.save(users);
                return user.getJWT();
            }
        }
        return "BAD";
    }

    private String getPayload(Request request) throws ParseException {
        String[] splittedHeader = request.headers("Authorization").split("\\s+");
        String jwtToken = splittedHeader[1];
        SignedJWT decodedJWT = SignedJWT.parse(jwtToken);
        String payload = decodedJWT.getPayload().toString();
        return payload;
    }

    public RoleType getLoggedUserRole(Request request) throws ParseException, IOException {
        String payload = getPayload(request);
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        return userService.getRoleTypeByUsername(username);
    }

    public UserDTO getUserInfo(Request req) throws Exception {
        String payload = getPayload(req);
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        return userService.getUserDTOByUsername(username);
    }

    public String getUsername(Request req) throws Exception {
        String id = req.params(":id");
        return userService.getUsernameById(id);
    }

    public void editUser(Request req) throws Exception {
        User user = makeUserFromReqBody(req);
        userDAO.updateUserInfo(user);
    }

    private User makeUserFromReqBody(Request req) {
        ArrayList<String> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
        String id = payload.get(0);
        String username = payload.get(1);
        String name = payload.get(2);
        String surname = payload.get(3);
        String sexString = payload.get(5);
        GenderType sex;
        if (sexString.equals("MALE")) {
            sex = GenderType.MALE;
        } else
        {
            sex = GenderType.FEMALE;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(payload.get(4), formatter);

        return makeUserWith(id, username, name, surname, sex, date);
    }

    private User makeUserWith(String id, String username, String name, String surname, GenderType sex, LocalDate date) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setGender(sex);
        user.setBirthDate(date);
        return user;
    }

    public String getUserId(Request req) throws Exception {
        UserDTO dto = getUserInfo(req);
        return dto.id;
    }
    
    public ArrayList<User> filter(Request req) throws Exception {
        Filter filter = gson.fromJson(req.body(), Filter.class);
        ArrayList<User> users = userDAO.getAll();
        ArrayList<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (name(filter, user)) {
                if (!filtered.contains(user)) {
                    filtered.add(user);
                }
            }
            if (!role(filter, user)) {
                if (filtered.contains(user)) {
                    filtered.remove(user);
                }
            }
            if (!customerType(filter, user)) {
                if (filtered.contains(user)) {
                    filtered.remove(user);
                }
            }
        }
        return sort(filter, filtered);
    }

    private Boolean name(Filter filter, User user) {
        return user.getName().toLowerCase().contains(filter.searchInput.toLowerCase()) ||
                user.getSurname().toLowerCase().contains(filter.searchInput.toLowerCase()) ||
                user.getUsername().toLowerCase().contains(filter.searchInput.toLowerCase());
    }

    private Boolean role(Filter filter, User user) {
        if (filter.role.equals("Role"))
            return true;
        return String.valueOf(user.getRole()) == roleNumToString(filter.role);
    }

    private String roleNumToString(String role)
    {
        switch(role){
            case "1" :
                return "ADMINISTRATOR";
            case "2" :
                return "MANAGER";
            case "3" :
                return "COACH";
            case "4" :
                return "CUSTOMER";
            default:
                return "";
        }
    }

    private Boolean customerType(Filter filter, User user) throws IOException {
        if (filter.type.equals("Customer type"))
            return true;
        if(user.getRole() != RoleType.CUSTOMER)
            return false;
        return (customerDAO.get(user.getId())).getType().getType().toString().equals(customerTypeToString(filter.type.toUpperCase()));
    }

    private String customerTypeToString(String type) {
        if(type.equals("1"))
            return "BRONZE";
        else if (type.equals("2"))
            return "SILVER";
        else if (type.equals("3"))
            return "GOLD";
        return "";
    }

    private ArrayList<User> sort(Filter filter, ArrayList<User> filtered) throws IOException {
        switch (filter.sort) {
            case "1":
                Collections.sort(filtered, Comparator.comparing(User::getName));
                break;
            case "2":
                Collections.sort(filtered, Comparator.comparing(User::getName, Comparator.reverseOrder()));
                break;
            case "3":
                Collections.sort(filtered, Comparator.comparing(User::getSurname));
                break;
            case "4":
                Collections.sort(filtered, Comparator.comparing(User::getSurname, Comparator.reverseOrder()));
                break;
//            case "5":
//                sortListByPoints(filtered,"asc");
//                break;
//            case "6":
//                sortListByPoints(filtered,"desc");
//                break;
            default:
                return filtered;
        }
        return filtered;
    }

    private void sortListByPoints(ArrayList<User> users,String type) throws IOException {
        ArrayList<Customer> sortedListCustomer = new ArrayList<Customer>();
        if (type.equals("asc")) {
            for (User user : users) {
                if (user.getRole() == RoleType.CUSTOMER) {
                    Customer customer = customerDAO.get(user.getId());
                    sortedListCustomer.add(customer);
                }
            }
            Collections.sort(sortedListCustomer, Comparator.comparing(Customer::getCollectedPoints));
            ArrayList<User> sortedUsers = new ArrayList<User>();
            for (int i = 0; i < sortedListCustomer.size(); i++) {
                for (User user : users) {
                    if (user.getRole() == RoleType.CUSTOMER && user.getUsername().equals(sortedListCustomer.get(i).getUsername())) {
                        sortedUsers.add(user);
                    }
                }
            }
            for (User user : users) {
                if (user.getRole() != RoleType.CUSTOMER)
                    sortedUsers.add(user);
            }
            users = new ArrayList<>(sortedUsers);
        } else if (type.equals("desc")) {
            for (User user : users) {
                if (user.getRole() == RoleType.CUSTOMER) {
                    Customer customer = customerDAO.get(user.getId());
                    sortedListCustomer.add(customer);
                }
            }
            Collections.sort(sortedListCustomer, Comparator.comparing(Customer::getCollectedPoints, Comparator.reverseOrder()));
            ArrayList<User> sortedUsers = new ArrayList<User>();
            for (int i = 0; i < sortedListCustomer.size(); i++) {
                for (User user : users) {
                    if (user.getRole() == RoleType.CUSTOMER && user.getUsername().equals(sortedListCustomer.get(i).getUsername())) {
                        sortedUsers.add(user);
                    }
                }
            }
            for (User user : users) {
                if (user.getRole() != RoleType.CUSTOMER)
                    sortedUsers.add(user);
            }
            users = new ArrayList<>(sortedUsers);
        }
    }

    public String changePassword(Request req) throws Exception {
        ArrayList<String> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
        User changedUser = new User();
        for (User user : userDAO.getAll()) {
            if (user.getId().equals(payload.get(0)) && user.getPassword().equals(payload.get(1))) {
                user.setPassword(payload.get(2));
                changedUser = user;
            }
        }
        ArrayList<User> allUsers = userDAO.getAllAndDeleted();
        for (User user : allUsers) {
            if (user.getId().equals(changedUser.getId())) {
                user.setPassword(changedUser.getPassword());
                userDAO.save(allUsers);
                return "SUCCESS";
            }
        }
        return "PASSWORD_EXCEPTION";
    }
}
