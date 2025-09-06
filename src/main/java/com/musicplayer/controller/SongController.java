package com.musicplayer.controller;

import com.musicplayer.model.Song;
import com.musicplayer.service.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService){
        this.songService = songService;
    }

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

     @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songService.saveSong(song);
    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
    }

    @PutMapping("/{id}")
public Song updateSong(@PathVariable Long id, @RequestBody Song song) {
    return songService.updateSong(id, song);
}

@GetMapping("/search/artist")
public List<Song> searchByArtist(@RequestParam String artist) {
    return songService.searchSongsByArtist(artist);
}

@GetMapping("/search/album")
public List<Song> searchByAlbum(@RequestParam String album) {
    return songService.searchSongsByAlbum(album);
}

@GetMapping("/search/title")
public List<Song> searchByTitle(@RequestParam String title) {
    return songService.searchSongsByTitle(title);
}


    
}
