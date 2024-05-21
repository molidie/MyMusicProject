package ar.edu.unnoba.pdyc.mymusic.service;


import ar.edu.unnoba.pdyc.mymusic.dto.CreateUserDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.UserDTO;
import ar.edu.unnoba.pdyc.mymusic.model.User;

import java.util.List;

public interface IUserService {
    public User createUser(CreateUserDTO createUserDTO);
    List<UserDTO> getAllUsers();
    boolean existsByEmail(String email);
    public void deleteUserById(Long userId);
}
