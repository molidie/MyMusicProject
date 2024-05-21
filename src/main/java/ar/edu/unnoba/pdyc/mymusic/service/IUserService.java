package ar.edu.unnoba.pdyc.mymusic.service;


import ar.edu.unnoba.pdyc.mymusic.dto.UserDTO;
import ar.edu.unnoba.pdyc.mymusic.model.User;

import java.util.List;

public interface IUserService {
    public User createUser(User user);
    List<UserDTO> getAllUsers();
    UserDTO convertToDTO(User user);
}
