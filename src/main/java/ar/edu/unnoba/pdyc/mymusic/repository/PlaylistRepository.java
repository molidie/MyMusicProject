package ar.edu.unnoba.pdyc.mymusic.repository;

import ar.edu.unnoba.pdyc.mymusic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.pdyc.mymusic.model.Playlist;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    public Playlist findAllById(Long id);
    List<Playlist> findByCreator(User creator);

}
