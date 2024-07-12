package ar.edu.unnoba.pdyc.mymusic.dto;

import java.util.List;

public class PlaylistDTO {

    private Long id;
    private String name;
    private int associatedSongs;
    private UserDTO creator;
    private List<SongDTO> songs; // Agregar esta línea para almacenar las canciones

    public PlaylistDTO() {
    }

    public PlaylistDTO(int associatedSongs, String name, Long id, UserDTO creator) {
        this.associatedSongs = associatedSongs;
        this.name = name;
        this.id = id;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAssociatedSongs() {
        return associatedSongs;
    }

    public void setAssociatedSongs(int associatedSongs) {
        this.associatedSongs = associatedSongs;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}
