package ar.edu.unnoba.pdyc.mymusic.service;

import org.jvnet.hk2.annotations.Service;
import java.util.List;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;


@Service
public interface  IPlaylistService {
    public Playlist create (Playlist playlist);
    public void delete (Long id);
    public List<Playlist> findAll();
    Playlist addSongToPlaylist(Long playlistId, Long songId);
}


