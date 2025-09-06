package com.musicplayer.service;

import com.musicplayer.model.Song;

import com.musicplayer.repository.SongRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository){
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs(){
        return songRepository.findAll();
    }

    public Song saveSong(Song song){
        return songRepository.save(song);
    }

    public void deleteSong(Long id){
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
            .orElseThrow(() -> new RuntimeException("Song not found with id " + id));
}

public List<Song> searchSongsByArtist(String artist) {
    return songRepository.findByArtistContainingIgnoreCase(artist);
}

public List<Song> searchSongsByAlbum(String album) {
    return songRepository.findByAlbumContainingIgnoreCase(album);
}

public List<Song> searchSongsByTitle(String title) {
    return songRepository.findByTitleContainingIgnoreCase(title);
}



    


    
}
