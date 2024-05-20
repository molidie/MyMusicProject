package ar.edu.unnoba.pdyc.mymusic.dto;


public class PlaylistDTO {

    private Long id;
    private String name;
    private int associatedSongs;

    public PlaylistDTO() {
    }

    public PlaylistDTO(int associatedSongs, String name, Long id) {
        this.associatedSongs = associatedSongs;
        this.name = name;
        this.id = id;
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

}
