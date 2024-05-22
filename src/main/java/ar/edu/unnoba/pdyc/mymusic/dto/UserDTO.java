package ar.edu.unnoba.pdyc.mymusic.dto;

import java.util.List;

public class UserDTO {
    private int id;
    private String email;

    public UserDTO() {
    }

    public UserDTO(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
