package ar.edu.unnoba.pdyc.mymusic.dto;


public class PlaylistDTO {

    private Long id;
    private String name;
    private int associatedSongs;
    private UserDTO creator;

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
}
