package com.musicplayer.controller;
import com.musicplayer.model.Song;
import com.musicplayer.service.SongService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService){
        this.songService = songService;
    }

    @GetMapping("/{id}")
    public Song getSongById(@PathVariable("id") Long id) {
    return songService.getSongById(id);
}
    @GetMapping("/paginated")
public Page<Song> getPaginatedSongs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

    return songService.getSongs(page, size, sortBy, direction);
}

@GetMapping
public List<Song> getAllSongs() {
    return songService.getAllSongs();
}

@PostMapping
@ResponseStatus(HttpStatus.CREATED) 
public Song addSong(@Valid @RequestBody Song song) {
    return songService.saveSong(song);
}


   @DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT) 
public void deleteSong(@PathVariable Long id) {
    songService.deleteSong(id);
}

    @PutMapping("/{id}")
public Song updateSong(
        @PathVariable Long id,
        @Valid @RequestBody Song updatedSong) {
    return songService.updateSong(id, updatedSong);
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

@GetMapping("/search")
public List<Song> searchSongs(
        @RequestParam(defaultValue = "") String title,
        @RequestParam(defaultValue = "") String artist,
        @RequestParam(defaultValue = "") String album) {

    return songService.searchSongs(title, artist, album);
}


    
}
