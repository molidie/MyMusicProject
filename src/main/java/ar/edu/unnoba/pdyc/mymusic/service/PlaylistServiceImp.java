package ar.edu.unnoba.pdyc.mymusic.service;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistServiceImp implements IPlaylistService {
    private final PlaylistRepository playlistRepository;
    private  SongRepository songRepository;
    private ModelMapper modelMapper;
    private SongServiceImp songService;

    public PlaylistServiceImp(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
        this.modelMapper=modelMapper=new ModelMapper();
    }

    @Override
    public Playlist create(Playlist playlist) {
        playlistRepository.save(playlist);
        return playlist;
    }

    @Override
    public void delete(Long id) {
        playlistRepository.deleteById(id);
    }

    @Override
    public List<Playlist> findAll() {
            return playlistRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistDTO> playlistDtos = playlists.stream().map(playlist -> {
            PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
            int songSize = playlist.getSongs().size();
            playlistDto.setCantidadDeCanciones(songSize);
            return playlistDto;
        }).toList();
        return Response.ok(playlistDtos).build();
    }

    @Override
    @Transactional
    public Response getAllSongsByList(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }
        // obtener canciones de la lista
        List<SongDTO> songs = playlist.getSongs().stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());

        if (songs.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " está vacía.")
                    .build();
        }
        return Response.ok(songs).build();
    }

    @Override
    @Transactional
    public Response addSongToPlaylist(Long playlistId, Long songId) {
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
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setCantidadDeCanciones(playlist.getSongs().size());
        return Response.ok(modelMapper.map(playlistDto, PlaylistDTO.class)).build();
    }
    @Override
    @Transactional
    public Response updatePlaylistName(Long id, String newName) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }

        if (newName == null || newName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre de la playlist no puede estar vacío.")
                    .build();
        }
        // Actualizar el nombre de la playlist
        playlist.setName(newName);
        playlistRepository.save(playlist);

        // Mapear y devolver la respuesta
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setCantidadDeCanciones(playlist.getSongs().size());
        return Response.ok(playlistDto).build();
    }

    @Override
    @Transactional
    public Response deleteSong(Long playlistId, Long songId) {
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
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setCantidadDeCanciones(playlist.getSongs().size());
        return Response.ok(modelMapper.map(playlistDto, PlaylistDTO.class)).build();
    }
    @Override
    @Transactional
    public Response deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("La playlist con la ID " + id + " no existe.")
                    .build(); // Playlist no encontrada
        }
        playlistRepository.delete(playlist);
        return Response.ok("La playlist con la ID " + id + " ha sido eliminada.").build();
    }



}
