package ar.edu.unnoba.pdyc.mymusic.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name= "nombre")
    private String name;
    @ManyToMany
    @JoinTable(name = "playlists_songs")
    private List<Song> songs;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;


    public Playlist() {
    }

    public Playlist(Long id, String name, List<Song> songs, User creator) {
        this.id = id;
        this.name = name;
        this.songs = songs;
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

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
