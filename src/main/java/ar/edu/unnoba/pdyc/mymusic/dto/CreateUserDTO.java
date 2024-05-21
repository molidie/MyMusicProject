package ar.edu.unnoba.pdyc.mymusic.dto;



import java.util.Set;

public class CreateUserDTO {
    private Long id;
    private String email;
    private String password;
    private Set<String> roles;

    public CreateUserDTO() {
    }

    public CreateUserDTO(Long id ,String password, Set<String> roles, String email) {
        this.id = id;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
