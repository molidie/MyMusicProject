package ar.edu.unnoba.pdyc.mymusic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import ar.edu.unnoba.pdyc.mymusic.model.Song;

@Service
public interface ISongService {

    public Song create(Song song);
    public void delete(Long id);
    public List<Song> findAll(); // Método agregado para obtener todas las canciones
    public Song getSongId(Long id);
}
