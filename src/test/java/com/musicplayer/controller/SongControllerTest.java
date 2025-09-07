package com.musicplayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicplayer.exception.SongNotFoundException;
import com.musicplayer.model.Song;
import com.musicplayer.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(SongController.class)
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    private Song song;
    private List<Song> multipleSongs;

    @BeforeEach
    void setUp() {
        song = new Song();
        song.setId(1L);
        song.setTitle("Dog Eat Dog II");
        song.setArtist("Odumodublvck");
        song.setAlbum("Eziokwu");
        song.setDuration(240);
        song.setReleaseYear(2023);

        
        Song song2 = new Song();
        song2.setId(2L);
        song2.setTitle("Declan Rice");
        song2.setArtist("Odumodublvck");
        song2.setAlbum("Eziokwu");
        song2.setDuration(200);
        song2.setReleaseYear(2023);

        multipleSongs = Arrays.asList(song, song2);
    }

    @Nested
    @DisplayName("GET /songs - Get songs")
    class GetSongsTests {

        @Test
        @DisplayName("Should return all songs when no pagination parameters")
        void testGetAllSongs() throws Exception {
            when(songService.getAllSongs()).thenReturn(multipleSongs);

            mockMvc.perform(get("/songs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].title").value("Dog Eat Dog II"))
                    .andExpect(jsonPath("$[0].artist").value("Odumodublvck"))
                    .andExpect(jsonPath("$[1].title").value("Declan Rice"))
                    .andExpect(jsonPath("$[1].artist").value("Odumodublvck"));

            verify(songService, times(1)).getAllSongs();
        }

        @Test
        @DisplayName("Should return paginated songs when pagination parameters provided")
        void testGetSongsWithPagination() throws Exception {
            Page<Song> page = new PageImpl<>(multipleSongs);
            when(songService.getSongs(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

            mockMvc.perform(get("/songs/paginated")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sortBy", "title")
                    .param("direction", "asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.content[0].title").value("Dog Eat Dog II"))
                    .andExpect(jsonPath("$.content[1].title").value("Declan Rice"))
                    .andExpect(jsonPath("$.totalElements").value(2));

            verify(songService, times(1)).getSongs(0, 10, "title", "asc");
        }

        @Test
        @DisplayName("Should return empty list when no songs exist")
        void testGetAllSongs_Empty() throws Exception {
            when(songService.getAllSongs()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/songs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(songService, times(1)).getAllSongs();
        }
    }

    @Nested
    @DisplayName("GET /songs/{id} - Get song by ID")
    class GetSongByIdTests {

        @Test
        @DisplayName("Should return song when valid ID provided")
        void testGetSongById_Success() throws Exception {
            when(songService.getSongById(1L)).thenReturn(song);

            mockMvc.perform(get("/songs/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Dog Eat Dog II"))
                    .andExpect(jsonPath("$.artist").value("Odumodublvck"))
                    .andExpect(jsonPath("$.album").value("Eziokwu"))
                    .andExpect(jsonPath("$.duration").value(240))
                    .andExpect(jsonPath("$.releaseYear").value(2023));

            verify(songService, times(1)).getSongById(1L);
        }

        @Test
        @DisplayName("Should return 404 when song not found")
        void testGetSongById_NotFound() throws Exception {
            when(songService.getSongById(999L)).thenThrow(new SongNotFoundException("Song with ID 999 not found"));

            mockMvc.perform(get("/songs/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Song with ID 999 not found"));

            verify(songService, times(1)).getSongById(999L);
        }

        @Test
        @DisplayName("Should return 400 when invalid ID format provided")
        void testGetSongById_InvalidId() throws Exception {
            mockMvc.perform(get("/songs/invalid"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /songs - Create song")
    class CreateSongTests {

        @Test
        @DisplayName("Should create song successfully with valid data")
        void testAddSong_Success() throws Exception {
            when(songService.saveSong(any(Song.class))).thenReturn(song);

            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isCreated()) 
                    .andExpect(jsonPath("$.title").value("Dog Eat Dog II"))
                    .andExpect(jsonPath("$.artist").value("Odumodublvck"))
                    .andExpect(jsonPath("$.album").value("Eziokwu"))
                    .andExpect(jsonPath("$.duration").value(240))
                    .andExpect(jsonPath("$.releaseYear").value(2023));

            verify(songService, times(1)).saveSong(any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when title is missing")
        void testAddSong_MissingTitle() throws Exception {
            song.setTitle("");

            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.title").value("Song title is required"));

            verify(songService, never()).saveSong(any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when artist is missing")
        void testAddSong_MissingArtist() throws Exception {
            song.setArtist("");

            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.artist").value("Artist name is required"));

            verify(songService, never()).saveSong(any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when duration is negative")
        void testAddSong_InvalidDuration() throws Exception {
            song.setDuration(-50);

            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.duration").value("Duration must be greater than zero"));

            verify(songService, never()).saveSong(any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when release year is invalid")
        void testAddSong_InvalidReleaseYear() throws Exception {
            song.setReleaseYear(1500);

            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.releaseYear").value("Release year cannot be before 1877"));

            verify(songService, never()).saveSong(any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when JSON is malformed")
        void testAddSong_MalformedJson() throws Exception {
            mockMvc.perform(post("/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ invalid json }"))
                    .andExpect(status().isBadRequest());

            verify(songService, never()).saveSong(any(Song.class));
        }
    }

    @Nested
    @DisplayName("PUT /songs/{id} - Update song")
    class UpdateSongTests {

        @Test
        @DisplayName("Should update song successfully when it exists")
        void testUpdateSong_Success() throws Exception {

            Song updatedSong = new Song();
            updatedSong.setTitle("Declan Rice");
            updatedSong.setArtist("Odumodublvck");
            updatedSong.setAlbum("Eziokwu");
            updatedSong.setDuration(200);
           updatedSong.setReleaseYear(2023);

            when(songService.updateSong(eq(1L), any(Song.class))).thenReturn(updatedSong);

            mockMvc.perform(put("/songs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Declan Rice"))
                    .andExpect(jsonPath("$.artist").value("Odumodublvck"))
                    .andExpect(jsonPath("$.album").value("Eziokwu"))
                    .andExpect(jsonPath("$.duration").value(200))
                    .andExpect(jsonPath("$.releaseYear").value(2023));

            verify(songService, times(1)).updateSong(eq(1L), any(Song.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent song")
        void testUpdateSong_NotFound() throws Exception {
            when(songService.updateSong(eq(999L), any(Song.class)))
                    .thenThrow(new SongNotFoundException("Song with ID 999 not found"));

            mockMvc.perform(put("/songs/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Song with ID 999 not found"));

            verify(songService, times(1)).updateSong(eq(999L), any(Song.class));
        }

        @Test
        @DisplayName("Should return 400 when validation fails during update")
        void testUpdateSong_ValidationFailed() throws Exception {
            song.setTitle("");
            song.setArtist("");
            song.setDuration(-5);
            song.setReleaseYear(1500);

            mockMvc.perform(put("/songs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(song)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Failed"))
                    .andExpect(jsonPath("$.errors.title").value("Song title is required"))
                    .andExpect(jsonPath("$.errors.artist").value("Artist name is required"))
                    .andExpect(jsonPath("$.errors.duration").value("Duration must be greater than zero"))
                    .andExpect(jsonPath("$.errors.releaseYear").value("Release year cannot be before 1877"));

            verify(songService, never()).updateSong(anyLong(), any(Song.class));

        }

       @Test
@DisplayName("Should return 400 when release year is in the future")
void testUpdateSong_FutureReleaseYear() throws Exception {
    Song songWithFutureYear = new Song();
    songWithFutureYear.setTitle("Test Song");
    songWithFutureYear.setArtist("Test Artist");
    songWithFutureYear.setAlbum("Test Album");
    songWithFutureYear.setDuration(200);
    songWithFutureYear.setReleaseYear(3000); 

    mockMvc.perform(put("/songs/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(songWithFutureYear)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Release year cannot be in the future")));

    verify(songService, never()).updateSong(anyLong(), any(Song.class));
}

    }

    @Nested
    @DisplayName("DELETE /songs/{id} - Delete song")
    class DeleteSongTests {

        @Test
        @DisplayName("Should delete song successfully")
        void testDeleteSong_Success() throws Exception {
            doNothing().when(songService).deleteSong(1L);

            mockMvc.perform(delete("/songs/1"))
                    .andExpect(status().isNoContent()); 

            verify(songService, times(1)).deleteSong(1L);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent song")
        void testDeleteSong_NotFound() throws Exception {
            doThrow(new SongNotFoundException("Song with ID 999 not found"))
                    .when(songService).deleteSong(999L);

            mockMvc.perform(delete("/songs/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Song with ID 999 not found"));

            verify(songService, times(1)).deleteSong(999L);
        }

        @Test
        @DisplayName("Should return 400 when invalid ID format provided")
        void testDeleteSong_InvalidId() throws Exception {
            mockMvc.perform(delete("/songs/invalid"))
                    .andExpect(status().isBadRequest());

            verify(songService, never()).deleteSong(anyLong());
        }
    }

    @Nested
    @DisplayName("Search endpoints")
    class SearchTests {

        @Test
        @DisplayName("GET /songs/search/title - Should search songs by title")
        void testSearchByTitle() throws Exception {
            when(songService.searchSongsByTitle("Dog")).thenReturn(multipleSongs);

            mockMvc.perform(get("/songs/search/title")
                    .param("title", "Dog"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].title").value("Dog Eat Dog II"));

            verify(songService, times(1)).searchSongsByTitle("Dog");
        }

        @Test
        @DisplayName("GET /songs/search/artist - Should search songs by artist")
        void testSearchByArtist() throws Exception {
            when(songService.searchSongsByArtist("Odumodublvck")).thenReturn(multipleSongs);

            mockMvc.perform(get("/songs/search/artist")
                    .param("artist", "Odumodublvck"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].artist").value("Odumodublvck"));

            verify(songService, times(1)).searchSongsByArtist("Odumodublvck");
        }

        @Test
        @DisplayName("GET /songs/search/album - Should search songs by album")
        void testSearchByAlbum() throws Exception {
            when(songService.searchSongsByAlbum("Eziokwu")).thenReturn(multipleSongs);

            mockMvc.perform(get("/songs/search/album")
                    .param("album", "Eziokwu"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].album").value("Eziokwu"));

            verify(songService, times(1)).searchSongsByAlbum("Eziokwu");
        }

        @Test
        @DisplayName("GET /songs/search - Should search songs with multiple criteria")
        void testSearchSongs() throws Exception {
            when(songService.searchSongs("Dog", "Odumodublvck", "Eziokwu"))
                    .thenReturn(Collections.singletonList(song));

            mockMvc.perform(get("/songs/search")
                    .param("title", "Dog")
                    .param("artist", "Odumodublvck")
                    .param("album", "Eziokwu"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].title").value("Dog Eat Dog II"));

            verify(songService, times(1)).searchSongs("Dog", "Odumodublvck", "Eziokwu");
        }

        @Test
        @DisplayName("GET /songs/search - Should return empty list when no results found")
        void testSearchSongs_Empty() throws Exception {
            when(songService.searchSongs("Nonexistent", "Artist", "Album"))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/songs/search")
                    .param("title", "Nonexistent")
                    .param("artist", "Artist")
                    .param("album", "Album"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(songService, times(1)).searchSongs("Nonexistent", "Artist", "Album");
        }
    }

    @Nested
    @DisplayName("Error handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle unexpected exceptions ")
        void testUnexpectedException() throws Exception {
            when(songService.getAllSongs()).thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(get("/songs"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("An unexpected error occurred"));

            verify(songService, times(1)).getAllSongs();
        }

        @Test
        @DisplayName("Should handle method not allowed")
        void testMethodNotAllowed() throws Exception {
            mockMvc.perform(patch("/songs/1"))
                    .andExpect(status().isMethodNotAllowed());
        }
    }
    }