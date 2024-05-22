package ar.edu.unnoba.pdyc.mymusic.resources;

import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import ar.edu.unnoba.pdyc.mymusic.service.ISongService;
import ar.edu.unnoba.pdyc.mymusic.service.SongServiceImp;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ar.edu.unnoba.pdyc.mymusic.model.Song;

@Component
@Path("/songs")
public class SongResource {
    @Autowired
    private SongServiceImp songService;

    private ModelMapper modelMapper = new ModelMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongs() {
        List<SongDTO> songDtos = songService.findAll().stream().map(song -> {
            SongDTO songDto = modelMapper.map(song, SongDTO.class);
            return songDto;
        }).toList();
        return Response.ok(songDtos).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSong(@RequestBody SongDTO songDto) {
        Song song = modelMapper.map(songDto, Song.class);
        Song createdSong = songService.create(song);
        return  Response.ok(modelMapper.map(createdSong, SongDTO.class)).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSong(@PathParam("id") Long id) {
        try {
            songService.delete(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot delete song because it is associated with one or more playlists.").build();
        }
    }
}