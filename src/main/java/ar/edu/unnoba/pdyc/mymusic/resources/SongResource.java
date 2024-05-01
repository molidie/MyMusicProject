package ar.edu.unnoba.pdyc.mymusic.resources;

import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.service.ISongService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Song;

@Component
@Path("/songs")
public class SongResource {

    @Autowired
    private ISongService songService;
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
}