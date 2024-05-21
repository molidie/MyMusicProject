package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.UserDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.CreateUserDTO;
import ar.edu.unnoba.pdyc.mymusic.model.*;
import ar.edu.unnoba.pdyc.mymusic.repository.RoleUserRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
    private UserRepository userRepository;
    private RoleUserRepository roleUserRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleUserRepository roleUserRepository) {
        this.userRepository = userRepository;
        this.roleUserRepository = roleUserRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundException("There are currently no registered users.");
        }
        return users.stream()
                .map(user -> {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    List<String> roles = user.getRoles().stream()
                            .map(RoleUser::getName)
                            .map(Enum::name)
                            .collect(Collectors.toList());
                    userDTO.setRoles(roles);
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(CreateUserDTO createUserDTO) {
        if (existsByEmail(createUserDTO.getEmail())) {
            throw new IllegalArgumentException("This email is already registered.");
        }
        Set<String> uniqueRoles = new HashSet<>(createUserDTO.getRoles());
        if (uniqueRoles.size() < createUserDTO.getRoles().size()) {
            throw new IllegalArgumentException("Roles cannot be duplicated.");
        }
        User user = modelMapper.map(createUserDTO, User.class);
        Set<RoleUser> roles = uniqueRoles.stream()
                .map(roleName -> {
                    ERole eRole = ERole.valueOf(roleName);
                    Optional<RoleUser> optionalRoleUser = roleUserRepository.findByName(eRole);
                    if (optionalRoleUser.isPresent()) {
                        return optionalRoleUser.get();
                    } else {
                        throw new RuntimeException("The role does not exist: " + eRole);
                    }
                })
                .collect(Collectors.toSet());
        user.setRoles(roles);
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



}
