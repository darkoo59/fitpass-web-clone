package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import dao.IUserDAO;
import dao.UserDAO;
import dto.UserDTO;
import io.jsonwebtoken.*;
import model.Credentials;
import model.User;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import utils.enums.GenderType;
import utils.enums.RoleType;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

public class AccountService {
    private IUserDAO userDAO;
    private UserService userService;
    private static String key = "fhjdfddjsaildhadlHHHjjFHaFHAkflfhAFKLJhfk";
    private  static String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
    private static byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key);
    private ObjectMapper mapper;

    public AccountService() {
        userDAO = new UserDAO();
        userService = new UserService();
        mapper = new ObjectMapper();
    }

    public void register(Request req) throws IOException {
        String name = req.queryParams("name");
        String surname = req.queryParams("surname");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String membership = req.queryParams("membership");
        String date = req.queryParams("date");
        LocalDate parsedDate = LocalDate.parse(date);
        String sex = req.queryParams("inlineRadioOptions");
        GenderType type = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        User newUser = new User(username, password, name, surname, type, parsedDate, RoleType.CUSTOMER);
        ArrayList<User> users = userDAO.getAll();
        users.add(newUser);
        userDAO.save(users);
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
        return null;
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
        String id = StringUtils.substringBetween(req.body(), "\"id\":\"", "\",");
        String username = StringUtils.substringBetween(req.body(), "\"username\":\"", "\",");
        String name = StringUtils.substringBetween(req.body(), "\"name\":\"", "\",");
        String surname = StringUtils.substringBetween(req.body(), "\"surname\":\"", "\",");
        String sexString = StringUtils.substringBetween(req.body(), "\"gender\":\"", "\",");
        GenderType sex;
        if (sexString.equals("MALE")) {
            sex = GenderType.MALE;
        } else
        {
            sex = GenderType.FEMALE;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(
                StringUtils.substringBetween(req.body(), "\"birthDate\":\"", "\""), formatter);

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
}
