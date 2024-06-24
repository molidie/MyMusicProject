package ar.edu.unnoba.pdyc.mymusic.dto;

import java.util.List;

public class UserDTO {
    private int id;
    private String email;
    private String firstname;
    private String lastname;

    public UserDTO() {
    }

    public UserDTO(String email, int id, String firstname, String lastname) {
        this.email = email;
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String name) {
        this.firstname = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
