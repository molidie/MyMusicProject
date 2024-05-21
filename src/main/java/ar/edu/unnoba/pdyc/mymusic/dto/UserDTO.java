package ar.edu.unnoba.pdyc.mymusic.dto;

import java.util.List;

public class UserDTO {
    private int id;
    private String email;
    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(int id, List<String> roles, String email) {
        this.id = id;
        this.roles = roles;
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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
