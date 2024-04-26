package ar.edu.unnoba.pdyc.mymusic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.pdyc.mymusic.model.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    public Playlist findAllById(Long id);

}
