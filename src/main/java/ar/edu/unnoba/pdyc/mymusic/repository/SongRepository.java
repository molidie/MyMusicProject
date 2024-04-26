package ar.edu.unnoba.pdyc.mymusic.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.unnoba.pdyc.mymusic.model.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
   public Song findSongById(Long id);
}
