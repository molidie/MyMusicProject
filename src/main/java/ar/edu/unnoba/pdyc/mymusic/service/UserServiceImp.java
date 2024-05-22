package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.CreateUserDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.UserDTO;
import ar.edu.unnoba.pdyc.mymusic.model.*;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundException("There are currently no registered users.");
        }
        return users.stream()
                .map(user -> {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(CreateUserDTO createUserDTO) {
        if (existsByEmail(createUserDTO.getEmail())) {
            throw new IllegalArgumentException("This email is already registered.");
        }
        User user = modelMapper.map(createUserDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(createUserDTO.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        userRepository.delete(userOptional.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(username);
    }
}
