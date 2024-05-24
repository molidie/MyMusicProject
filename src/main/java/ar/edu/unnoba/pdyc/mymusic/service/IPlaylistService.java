package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import jakarta.ws.rs.core.Response;
import org.jvnet.hk2.annotations.Service;
import java.util.List;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;

@Service
public interface  IPlaylistService {
    public PlaylistDTO createPlaylist(PlaylistDTO playlistDto);
    Response getAllSongsByList(Long id);
    Response addSongToPlaylist(Long playlistId, Long songId);
    Response deleteSong(Long playlistId, Long songId);
    Response updatePlaylistName(Long id, String newName);
    Response getPlaylists();
    Response deletePlaylist(Long id);
}


