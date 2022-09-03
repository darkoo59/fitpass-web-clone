package dto;

import utils.enums.GenderType;

import java.time.LocalDate;

public class UserDTO {
    public String id;
    public String username;
    public String name;
    public String surname;
    public GenderType gender;
    public LocalDate birthDate;
}
