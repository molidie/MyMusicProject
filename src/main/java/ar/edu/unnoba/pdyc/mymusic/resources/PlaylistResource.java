
package ar.edu.unnoba.pdyc.mymusic.resources;
import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.mymusic.service.SongServiceImp;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.service.PlaylistServiceImp;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
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
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists() {
        ModelMapper modelMapper = new ModelMapper();
        List<Playlist> playlists = playlistService.findAll();
        List<PlaylistDTO> playlistDtos = playlists.stream().map(playlist -> {
            PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
            playlistDto.setId(playlist.getId());
            playlistDto.setName(playlist.getName());
            int songSize = playlist.getSongs().size();
            playlistDto.setCantidadDeCanciones(songSize);

            return playlistDto;

        }).toList();
        return Response.ok(playlistDtos).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@RequestBody PlaylistDTO playlistDto){
        ModelMapper modelMapper = new ModelMapper();
        Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
        playlistService.create(playlist);
        playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setName(playlist.getName());
        playlistDto.setId(playlist.getId());
        return Response.ok(playlistDto).build();
    }
    @POST
    @Path("/{id}/songs")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSong(@PathParam("id") Long idP, @RequestBody Map<String, Object> requestBody) {
        Playlist playlist = playlistRepository.findById(idP).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + idP + " no existe.")
                    .build(); // Playlist no encontrada
        }

        Integer songId = (Integer) requestBody.get("songId");
        Long songIdLong = songId.longValue();

        Song song = songService.getSongId(songIdLong);
        if (song == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La canción con la ID " + songIdLong + " no existe.")
                    .build(); // Canción no encontrada
        }

        // Verificar si la canción ya está en la lista de reproducción
        if (playlist.getSongs().contains(song)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La canción ya está en la lista de reproducción.")
                    .build(); // La canción ya está en la lista de reproducción
        }

        // Agregar la canción a la lista de reproducción
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);

        // Mapear y devolver la respuesta
        PlaylistDTO playlistDto = mapPlaylistToDTO(playlist);
        return Response.ok(playlistDto).build();
    }

    private PlaylistDTO mapPlaylistToDTO(Playlist playlist) {
        ModelMapper modelMapper = new ModelMapper();
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setCantidadDeCanciones(playlist.getSongs().size());
        return playlistDto;
    }




}