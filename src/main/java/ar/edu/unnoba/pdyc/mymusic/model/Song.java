package ar.edu.unnoba.pdyc.mymusic.model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "nombre")
    private String name;

    @Column (name = "autor")
    private String author;

    @Column (name = "genero")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;

    public Song(Long id, String name, String author, Genre genre, List<Playlist> playlists) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.playlists = playlists;
    }

    public Song() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    
}
