package ar.edu.unnoba.pdyc.mymusic.service;

import java.util.List;

import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import org.springframework.stereotype.Service;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import jakarta.ws.rs.NotFoundException;

@Service
public class PlaylistServiceImp implements IPlaylistService {
    private final PlaylistRepository playlistRepository;
    private  SongRepository songRepository;

    public PlaylistServiceImp(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
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

}
