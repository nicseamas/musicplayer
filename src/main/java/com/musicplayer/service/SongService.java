package com.musicplayer.service;
import com.musicplayer.exception.SongNotFoundException;
import com.musicplayer.model.Song;
import com.musicplayer.repository.SongRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository){
        this.songRepository = songRepository;
    }

   public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with ID " + id + " not found"));
    }

    public Page<Song> getSongs(int page, int size, String sortBy) {
    if (size <= 0) size = 10; 
    if (page < 0) page = 0;   
    if (sortBy == null || sortBy.isEmpty()) sortBy = "title"; // Default sorting
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return songRepository.findAll(pageable);
}


    public Song saveSong(Song song){
        return songRepository.save(song);
    }

   public void deleteSong(Long id) {
        if (!songRepository.existsById(id)) {
            throw new SongNotFoundException("Song with ID " + id + " not found");
        }
        songRepository.deleteById(id);
    }

    public Song updateSong(Long id, Song updatedSong) {
    return songRepository.findById(id)
            .map(song -> {
                song.setTitle(updatedSong.getTitle());
                song.setArtist(updatedSong.getArtist());
                song.setAlbum(updatedSong.getAlbum());
                song.setDuration(updatedSong.getDuration());
                song.setReleaseYear(updatedSong.getReleaseYear());
                return songRepository.save(song);
            })
            .orElseThrow(() -> new SongNotFoundException("Song with ID " + id + " not found"));
}

public List<Song> searchSongsByArtist(String artist) {
        List<Song> songs = songRepository.findByArtistContainingIgnoreCase(artist);
        if (songs.isEmpty()) {
            throw new SongNotFoundException("No songs found for artist: " + artist);
        }
        return songs;
    }

public List<Song> searchSongsByAlbum(String album) {
        List<Song> songs = songRepository.findByAlbumContainingIgnoreCase(album);
        if (songs.isEmpty()) {
            throw new SongNotFoundException("No songs found for album: " + album);
        }
        return songs;
    }

public List<Song> searchSongsByTitle(String title) {
        List<Song> songs = songRepository.findByTitleContainingIgnoreCase(title);
        if (songs.isEmpty()) {
            throw new SongNotFoundException("No songs found with title: " + title);
        }
        return songs;
    }



    


    
}
