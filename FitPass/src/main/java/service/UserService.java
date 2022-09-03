package service;

import dao.UserDAO;
import dto.UserDTO;
import model.User;
import utils.enums.RoleType;

import java.io.IOException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public RoleType getRoleTypeByUsername(String username) throws IOException {
        for (User user : userDAO.getAll()) {
            if(user.getUsername().equals(username)){
            switch (user.getRole()) {
                case CUSTOMER:
                    return RoleType.CUSTOMER;
                case ADMINISTRATOR:
                    return RoleType.ADMINISTRATOR;
                case COACH:
                    return RoleType.COACH;
                case MANAGER:
                    return RoleType.MANAGER;
                default:
                    return null;
            }
            }
        }
        return null;
    }

    public UserDTO getUserDTOByUsername(String username) throws Exception {
        UserDTO userDTO = new UserDTO();
        for (User user : userDAO.getAll()) {
            if (user.getUsername().equals(username)) {
                mapUserToDTO(userDTO, user);
            }
        }
        return userDTO;
    }

    private void mapUserToDTO(UserDTO userDTO, User user) {
        userDTO.id = user.getId();
        userDTO.username = user.getUsername();
        userDTO.name = user.getName();
        userDTO.surname = user.getSurname();
        userDTO.gender = user.getGender();
        userDTO.birthDate = user.getBirthDate();
    }
}