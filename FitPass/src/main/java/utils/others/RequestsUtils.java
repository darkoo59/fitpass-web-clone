package utils.others;

import com.nimbusds.jwt.SignedJWT;
import dao.UserDAO;
import model.TrainingHistory;
import model.User;
import spark.Request;
import utils.enums.RoleType;

import javax.management.relation.Role;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class RequestsUtils {
    public static String getPayload(Request request) throws ParseException {
        String[] splittedHeader = request.headers("Authorization").split("\\s+");
        String jwtToken = splittedHeader[1];
        SignedJWT decodedJWT = SignedJWT.parse(jwtToken);
        String payload = decodedJWT.getPayload().toString();
        return payload;
    }

    public static String getIdFromPayload(String payload) throws IOException {
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        String id = "";
        UserDAO userDAO = new UserDAO();
        ArrayList<User> allUsers = userDAO.getAll();
        for(User user : allUsers)
        {
            if(user.getUsername().equals(username))
            {
                id = user.getId();
                break;
            }
        }
        return id;
    }

    public static RoleType getRoleFromPayload(String payload) throws IOException {
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        RoleType role = null;
        UserDAO userDAO = new UserDAO();
        ArrayList<User> allUsers = userDAO.getAll();
        for(User user : allUsers)
        {
            if(user.getUsername().equals(username))
            {
                role = user.getRole();
                break;
            }
        }
        return role;
    }
}
