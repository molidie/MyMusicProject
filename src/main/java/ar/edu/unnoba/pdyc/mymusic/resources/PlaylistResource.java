
package ar.edu.unnoba.pdyc.mymusic.resources;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.mymusic.service.SongServiceImp;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.service.PlaylistServiceImp;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;


@Component
@Path("/playlists")
public class PlaylistResource {

    private PlaylistServiceImp playlistService;
    private SongServiceImp songService;
    private ModelMapper modelMapper;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistResource(PlaylistServiceImp playlistService, SongServiceImp songService) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.modelMapper = new ModelMapper();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists() {
        return playlistService.getPlaylists();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@RequestBody PlaylistDTO playlistDto) {
        try {
            PlaylistDTO createdPlaylistDto = playlistService.createPlaylist(playlistDto);
            return Response.ok(createdPlaylistDto).build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("The user is not authenticated.")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\": \"The user is not authenticated.\"}")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"" + e.getMessage() + "\"}")
                        .build();
            }
        }
    }


    @POST
    @Path("/{id}/songs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSongToPlaylist(@PathParam("id") Long playlistId, Map<String, Long> requestBody) {
        Long songId = requestBody.get("songId");
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @DELETE
    @Path("/{id}/songs/{song_id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSong(@PathParam("id") Long playlistId, @PathParam("song_id") Long songId) {
        return playlistService.deleteSong(playlistId, songId);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@PathParam("id") Long id, @RequestBody Map<String, Object> requestBody) {
        String newName = (String) requestBody.get("name");
        return playlistService.updatePlaylistName(id, newName);
    }

    @GET
    @Path("/{id}/songs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongsByList(@PathParam("id") Long id) {
        return playlistService.getAllSongsByList(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlaylist(@PathParam("id") Long id) {
        return playlistService.deletePlaylist(id);
    }
}
