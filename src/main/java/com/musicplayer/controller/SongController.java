package com.musicplayer.controller;
import com.musicplayer.model.Song;
import com.musicplayer.service.SongService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService){
        this.songService = songService;
    }

    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
    return songService.getSongById(id);
}
    @GetMapping("/paginated")
       public Page<Song> getPaginatedSongs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sortBy) {
    return songService.getSongs(page, size, sortBy);
}

    @PostMapping
    public Song addSong(@Valid @RequestBody Song song) {
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
