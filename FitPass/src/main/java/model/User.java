package model;

import utils.enums.GenderType;
import utils.enums.RoleType;

import java.text.Format;
import java.time.LocalDate;
import java.util.Date;

public class User {

    private String id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private GenderType gender;
    private LocalDate birthDate;
    private RoleType role;
    private String JWT;

    public User()
    {

    }
    public User(String username, String password, String name, String surname, GenderType gender, LocalDate birthDate,
                RoleType role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getJWT() {return JWT;}

    public void setJWT(String JWT) { this.JWT = JWT; }
}
