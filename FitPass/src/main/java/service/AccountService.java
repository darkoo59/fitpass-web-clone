package service;

import com.esotericsoftware.kryo.util.IntMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import dao.IDao;
import dao.UserDAO;
import io.jsonwebtoken.*;
import model.Credentials;
import model.User;
import org.apache.spark.sql.catalyst.expressions.Base64;
import scala.util.parsing.json.JSONObject;
import scala.util.parsing.json.JSONObject$;
import spark.Request;
import utils.enums.GenderType;
import utils.enums.RoleType;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Signed;
import javax.management.relation.Role;
import javax.xml.bind.DatatypeConverter;

import static utils.others.Decode.decode;

public class AccountService {
    private IDao userDAO;
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
        Credentials cred = new Credentials();
        cred = mapper.readValue(request.body(),Credentials.class);
        System.out.println("Username = "+cred.getUsername()+" password = "+cred.getPassword());
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

    public RoleType getLoggedUserRole(Request request) throws ParseException, IOException {
        String[] splittedHeader = request.headers("Authorization").split("\\s+");
        String jwtToken = splittedHeader[1];
        SignedJWT decodedJWT = SignedJWT.parse(jwtToken);
        String payload = decodedJWT.getPayload().toString();
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        System.out.println(username);
        return userService.getRoleTypeByUsername(username);
    }
    public User loginJWT(User user) {

// Token je validan 10 sekundi!

        String jws =
                Jwts.builder().setSubject(user.getUsername()).
                        setExpiration(new Date(new Date().getTime() +
                                1000*10L)).
                        setIssuedAt(new Date()).
                        signWith(SignatureAlgorithm.HS512,secretBytes).
                        compact();

        user.setJWT(jws);

        System.out.println("Retuned JWT: " + jws);
        return user;
    }
}
