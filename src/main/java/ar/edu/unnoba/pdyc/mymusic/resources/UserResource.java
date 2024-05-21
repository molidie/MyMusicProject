package ar.edu.unnoba.pdyc.mymusic.resources;

import ar.edu.unnoba.pdyc.mymusic.dto.CreateUserDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.UserDTO;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.service.UserServiceImp;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/users")
public class UserResource {

    private UserServiceImp userService;

    @Autowired
    public UserResource(UserServiceImp userService) {
        this.userService = userService;
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
        try {
            userService.createUser(createUserDTO);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully.\"}").build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        } catch (RuntimeException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") Long userId) {
        userService.deleteUserById(userId);
        return Response.ok().entity("{\"message\": \"User deleted successfully.\"}").build();
    }
}
