
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
import java.util.Map;
import java.util.stream.Collectors;


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
        List<Playlist> playlists = playlistService.findAll();
        List<PlaylistDTO> playlistDtos = playlists.stream().map(playlist -> {
            PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
            int songSize = playlist.getSongs().size();
            playlistDto.setCantidadDeCanciones(songSize);

            return playlistDto;

        }).toList();
        return Response.ok(playlistDtos).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@RequestBody PlaylistDTO playlistDto) {
        Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
        Playlist createdPlaylist = playlistService.create(playlist);
        return Response.ok(modelMapper.map(createdPlaylist, PlaylistDTO.class)).build();
    }

    @POST
    @Path("/{id}/songs")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSongToPlaylist(@PathParam("id") Long idP, @RequestBody Map<String, Object> requestBody) {
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


    @DELETE
    @Path("/{id}/songs/{song_id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSong(@PathParam("id") Long playlistId, @PathParam("song_id") Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + playlistId + " no existe.")
                    .build(); // Playlist no encontrada
        }

        Song song = songService.getSongId(songId);
        if (song == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La canción con la ID " + songId + " no existe.")
                    .build(); // Canción no encontrada
        }

        // Verificar si la canción está en la lista de reproducción
        if (!playlist.getSongs().contains(song)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La canción no está en la lista de reproducción.")
                    .build(); // La canción no está en la lista de reproducción
        }

        // Quitar la canción de la lista de reproducción
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);

        // Mapear y devolver la respuesta
        PlaylistDTO playlistDto = mapPlaylistToDTO(playlist);
        return Response.ok(playlistDto).build();
    }
    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@RequestBody Map<String, Object> requestBody,@PathParam("id") Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }

        String newName = (String) requestBody.get("name");
        if (newName == null || newName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre de la playlist no puede estar vacío.")
                    .build(); // El nombre de la playlist no puede estar vacío
        }

        // Actualizar el nombre de la playlist
        playlist.setName(newName);
        playlistRepository.save(playlist);

        // Mapear y devolver la respuesta
        PlaylistDTO playlistDto = mapPlaylistToDTO(playlist);
        return Response.ok(playlistDto).build();
    }
    @GET
    @Path("/{id}/songs")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongsByList(@PathParam("id") Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }

        // Obtener todas las canciones de la lista de reproducción
        List<SongDTO> songs = playlist.getSongs().stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());

            if(songs.isEmpty())
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("La playlist con la ID " + id + " está vacía.")
                        .build();
        return Response.ok(songs).build();

    }

    @DELETE
    @Path("/{id}")
    public Response deletePlaylist(@PathParam("id") Long id){
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }
        playlistRepository.delete(playlist);
//        return Response.ok(playlist)
//                .entity("La playlist fue eliminada correctamente. SOS UN CAPO DIEGLOCK")
//                .build(); //Playlist eliminada
        return Response.ok("La playlist con la ID " + id + " ha sido eliminada.").build();
    }

    //ESTA FUNCION ES OTRA ALTERNATIVA QUE TAMBIEN FUNCIONA
//    La diferencia es que en la funcion de arriba uso Map<String, Object>
//    todos los valores del mapa se consideran como objetos en Java (Object)
//    cuando se extrae el nombre de la playlist del mapa utilizando requestBody.get("name"),
//    el valor devuelto por get("name") es de tipo Object, ya que el valor asociado con la
//    clave "name" en el mapa se considera como un objeto genérico.
//
//    Para obtener el nombre de la playlist como un String, necesitas realizar un casting explícito del
//    objeto devuelto por get("name") a String. Esto se hace utilizando (String) antes de
//    requestBody.get("name"). El casting explícito convierte el objeto de tipo Object a un objeto
//    de tipo String, lo que te permite tratarlo como un String en el código.




//    @PUT
//    @Path("/{id}")
//    @Transactional
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updatePlaylistName(@PathParam("id") Long id, Map<String, String> requestBody) {
//        // Verificar si la playlist existe
//        Playlist playlist = playlistRepository.findById(id).orElse(null);
//        if (playlist == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("La playlist con la ID " + id + " no existe.")
//                    .build();
//        }
//
//        // Obtener el nuevo nombre de la playlist del cuerpo de la solicitud
//        String newName = requestBody.get("name");
//        if (newName == null || newName.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("El nombre de la playlist no puede estar vacío.")
//                    .build();
//        }
//
//        // Actualizar el nombre de la playlist
//        playlist.setName(newName);
//        playlistRepository.save(playlist);
//
//        // Mapear la playlist actualizada a un DTO
//        PlaylistDTO playlistDto = mapPlaylistToDTO(playlist);
//
//        // Devolver la respuesta con la playlist actualizada
//        return Response.ok(playlistDto).build();
//    }

}
