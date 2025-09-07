package com.musicplayer.repository;

import com.musicplayer.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Song Repository Tests")
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    private Song song1;
    private Song song2;

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();

        song1 = new Song();
        song1.setTitle("Dog Eat Dog II");
        song1.setArtist("Odumodublvck");
        song1.setAlbum("Eziokwu");
        song1.setDuration(240);
        song1.setReleaseYear(2023);

        song2 = new Song();
        song2.setTitle("Declan Rice");
        song2.setArtist("Odumodublvck");
        song2.setAlbum("Eziokwu");
        song2.setDuration(200);
        song2.setReleaseYear(2023);

        song1 = songRepository.save(song1);
        song2 = songRepository.save(song2);
    }

    @Nested
    @DisplayName("Save & Find by ID Tests")
    class SaveAndFindTests {

        @Test
        @DisplayName("Should save and retrieve song by ID")
        void testSaveAndFindById() {
            Optional<Song> found = songRepository.findById(song1.getId());
            assertTrue(found.isPresent());
            assertEquals("Dog Eat Dog II", found.get().getTitle());
        }

        @Test
        @DisplayName("Should return empty when song does not exist")
        void testFindById_NotFound() {
            Optional<Song> found = songRepository.findById(999L);
            assertTrue(found.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by Title Tests")
    class FindByTitleTests {

        @Test
        @DisplayName("Should find songs by exact title")
        void testFindByExactTitle() {
            List<Song> results = songRepository.findByTitleContainingIgnoreCase("Dog Eat Dog II");
            assertEquals(1, results.size());
            assertEquals("Dog Eat Dog II", results.get(0).getTitle());
        }

        @Test
        @DisplayName("Should find songs by partial title")
        void testFindByPartialTitle() {
            List<Song> results = songRepository.findByTitleContainingIgnoreCase("Dog");
            assertEquals(1, results.size());
            assertEquals("Dog Eat Dog II", results.get(0).getTitle());
        }

        @Test
        @DisplayName("Should return empty list when title not found")
        void testFindByTitle_NotFound() {
            List<Song> results = songRepository.findByTitleContainingIgnoreCase("Unknown");
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by Artist Tests")
    class FindByArtistTests {

        @Test
        @DisplayName("Should find songs by exact artist")
        void testFindByExactArtist() {
            List<Song> results = songRepository.findByArtistContainingIgnoreCase("Odumodublvck");
            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Should find songs by partial artist name")
        void testFindByPartialArtist() {
            List<Song> results = songRepository.findByArtistContainingIgnoreCase("Odu");
            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Should return empty list when artist not found")
        void testFindByArtist_NotFound() {
            List<Song> results = songRepository.findByArtistContainingIgnoreCase("Unknown");
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find by Album Tests")
    class FindByAlbumTests {

        @Test
        @DisplayName("Should find songs by album")
        void testFindByAlbum() {
            List<Song> results = songRepository.findByAlbumContainingIgnoreCase("Eziokwu");
            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Should return empty list when album not found")
        void testFindByAlbum_NotFound() {
            List<Song> results = songRepository.findByAlbumContainingIgnoreCase("Unknown");
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find All Songs Tests")
    class FindAllSongsTests {

        @Test
        @DisplayName("Should return all songs")
        void testFindAllSongs() {
            List<Song> results = songRepository.findAll();
            assertEquals(2, results.size());
        }
    }

    @Nested
    @DisplayName("Delete Song Tests")
    class DeleteSongTests {

        @Test
        @DisplayName("Should delete song successfully")
        void testDeleteSong() {
            songRepository.deleteById(song1.getId());
            Optional<Song> found = songRepository.findById(song1.getId());
            assertTrue(found.isEmpty());
        }

        @Test
        @DisplayName("Deleting non-existing song should not fail")
        void testDeleteSong_NotFound() {
            assertDoesNotThrow(() -> songRepository.deleteById(999L));
        }
    }

    @Nested
    @DisplayName("Combined Search Tests")
    class CombinedSearchTests {

        @Test
        @DisplayName("Should find songs by title, artist and album")
        void testCombinedSearch() {
            List<Song> results = songRepository
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                            "Dog", "Odumodublvck", "Eziokwu");
            assertEquals(1, results.size());
            assertEquals("Dog Eat Dog II", results.get(0).getTitle());
        }

        @Test
        @DisplayName("Should return empty when no combined match")
        void testCombinedSearch_NotFound() {
            List<Song> results = songRepository
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                            "Unknown", "Artist", "Album");
            assertTrue(results.isEmpty());
        }
    }
}
