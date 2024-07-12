package ar.edu.unnoba.pdyc.mymusic.service;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistServiceImp implements IPlaylistService {
    private final PlaylistRepository playlistRepository;
    private ModelMapper modelMapper;
    private SongServiceImp songService;
    private  UserRepository userRepository;

    public PlaylistServiceImp(PlaylistRepository playlistRepository, UserRepository userRepository, SongServiceImp songService) {
        this.playlistRepository = playlistRepository;
        this.modelMapper=modelMapper=new ModelMapper();
        this.userRepository=userRepository;
        this.songService = songService;
    }

    //CREATE PLAYSLISTS

    @Override
    public PlaylistDTO createPlaylist(PlaylistDTO playlistDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("The user is not authenticated.");
        }

        String creatorEmail;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            creatorEmail = ((UserDetails) principal).getUsername();
        } else {
            creatorEmail = principal.toString();
        }

        User user = userRepository.findByEmail(creatorEmail);
        if (user == null) {
            throw new RuntimeException("The user with the email " + creatorEmail + " does not exist.");
        }

        Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
        playlist.setCreator(user);
        Playlist createdPlaylist = playlistRepository.save(playlist);
        return modelMapper.map(createdPlaylist, PlaylistDTO.class);
    }



    //GET PLAYLISTS

    @Override
    @Transactional(readOnly = true)
    public Response getPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistDTO> playlistDTOS = playlists.stream().map(playlist -> {
            PlaylistDTO playlistDTO = modelMapper.map(playlist, PlaylistDTO.class);
            int songSize = playlist.getSongs().size();
            playlistDTO.setAssociatedSongs(songSize);
            return playlistDTO;
        }).toList();
        return Response.ok(playlistDTOS).build();
    }


    //GET SONGS FROM PLAYLISTS

    @Override
    @Transactional
    public Response getAllSongsByList(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The playlist with the ID " + id + " does not exist.")
                    .build();
        }
        PlaylistDTO playlistDTO = modelMapper.map(playlist, PlaylistDTO.class);
        if (playlistDTO.getSongs().isEmpty()) {
            return Response.status(Response.Status.OK)
                    .entity(playlistDTO)
                    .build();
        }
        return Response.ok(playlistDTO).build();
    }


    //ADD SONG TO PLAYLISTS

    @Override
    @Transactional
    public Response addSongToPlaylist(Long playlistId, Long songId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("The user is not authenticated.")
                    .build();
        }
        String authenticatedUserEmail = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            authenticatedUserEmail = ((UserDetails) principal).getUsername();
        } else {
            authenticatedUserEmail = principal.toString();
        }
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The playlist with the ID " + playlistId + " does not exist.")
                    .build();
        }
        String playlistCreatorEmail = playlist.getCreator().getEmail();
        if (!authenticatedUserEmail.equals(playlistCreatorEmail)) {
            throw new WebApplicationException("Only the creator of the playlist can add songs to it.", Response.Status.FORBIDDEN);
        }
        Song song = songService.getSongId(songId);
        if (song == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The song with the ID " + songId + " does not exist.")
                    .build();
        }
        if (playlist.getSongs().contains(song)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The song is already in the playlist.")
                    .build();
        }
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setAssociatedSongs(playlist.getSongs().size());
        return Response.ok(modelMapper.map(playlistDto, PlaylistDTO.class)).build();
    }


    //UPDATE NAME TO PLAYLIST


    @Override
    @Transactional
    public Response updatePlaylistName(Long id, String newName) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The playlist with the ID \"" + id + " does not exist.")
                    .build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                authenticatedUserEmail = ((UserDetails) principal).getUsername();
            } else {
                authenticatedUserEmail = principal.toString();
            }
        }
        User creator = playlist.getCreator();
        if (creator == null || !authenticatedUserEmail.equals(creator.getEmail())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Only the playlist creator can update its name.")
                    .build();
        }
        if (newName == null || newName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The playlist name cannot be empty.")
                    .build();
        }
        playlist.setName(newName);
        playlistRepository.save(playlist);
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setAssociatedSongs(playlist.getSongs().size());
        return Response.ok(playlistDto).build();
    }


//    DELETE SONG FROM A PLAYLIST

    @Override
    @Transactional
    public Response deleteSong(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The playlist with the ID " + playlistId + " does not exist.")
                    .build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            authenticatedUserEmail = principal instanceof UserDetails ?
                    ((UserDetails) principal).getUsername() : principal.toString();
        }
        User creator = playlist.getCreator();
        if (creator == null || !authenticatedUserEmail.equals(creator.getEmail())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Only the playlist creator can remove songs from the playlist.")
                    .build();
        }
        Song song = songService.getSongId(songId);
        if (song == null || !playlist.getSongs().contains(song)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The song with the ID " + songId + " does not exist in the playlist.")
                    .build();
        }
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);
        PlaylistDTO playlistDto = modelMapper.map(playlist, PlaylistDTO.class);
        playlistDto.setAssociatedSongs(playlist.getSongs().size());
        return Response.ok(playlistDto).build();
    }

//    DELETE PLAYLIST

    @Override
    @Transactional
    public Response deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The playlist with the ID " + id + " does not exist.")
                    .build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            authenticatedUserEmail = principal instanceof UserDetails ?
                    ((UserDetails) principal).getUsername() : principal.toString();
        }
        User creator = playlist.getCreator();
        if (creator == null || !authenticatedUserEmail.equals(creator.getEmail())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Only the playlist creator can delete it.")
                    .build();
        }
        playlistRepository.delete(playlist);
        return Response.ok("The playlist with the ID " + id + " has been deleted.").build();
    }

    //GET PLAYLISTS BY USERS

    @Override
    @Transactional(readOnly = true)
    public Response getPlaylistsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("The user is not authenticated.")
                    .build();
        }
        String authenticatedUserEmail = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            authenticatedUserEmail = ((UserDetails) principal).getUsername();
        } else {
            authenticatedUserEmail = principal.toString();
        }
        User user = userRepository.findByEmail(authenticatedUserEmail);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist.")
                    .build();
        }
        List<Playlist> playlists = playlistRepository.findByCreator(user);
        List<PlaylistDTO> playlistDTOS = playlists.stream()
                .map(playlist -> {
                    PlaylistDTO playlistDTO = modelMapper.map(playlist, PlaylistDTO.class);
                    int songSize = playlist.getSongs().size();
                    playlistDTO.setAssociatedSongs(songSize);
                    return playlistDTO;
                })
                .collect(Collectors.toList());
        return Response.ok(playlistDTOS).build();
    }
}







