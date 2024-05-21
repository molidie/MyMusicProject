package ar.edu.unnoba.pdyc.mymusic.resources;

import ar.edu.unnoba.pdyc.mymusic.dto.*;
import ar.edu.unnoba.pdyc.mymusic.model.*;
import ar.edu.unnoba.pdyc.mymusic.repository.*;
import ar.edu.unnoba.pdyc.mymusic.service.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/users")
public class UserResource {

    private  UserServiceImp userService;
    private final UserServiceImp userServiceImp;
    private  ModelMapper modelMapper;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    public UserResource(UserServiceImp userService, UserServiceImp userServiceImp) {
        this.userService = userService;
        this.userServiceImp = userServiceImp;
        this.modelMapper = new ModelMapper();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return Response.ok(users).build();
        } catch (NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"The user list is empty.\"}")
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserDTO createUserDTO) {
        User user = modelMapper.map(createUserDTO, User.class);
        Set<RoleUser> roles = createUserDTO.getRoles().stream()
                .map(roleName -> {
                    ERole eRole = ERole.valueOf(roleName);
                    Optional<RoleUser> optionalRoleUser = roleUserRepository.findByName(eRole);
                    if (optionalRoleUser.isPresent()) {
                        return optionalRoleUser.get();
                    } else {
                        throw new RuntimeException("El rol no existe: " + eRole);
                    }
                })
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.createUser(user);
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"User created successfully.\"}").build();
    }
}
