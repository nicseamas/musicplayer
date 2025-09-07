package com.musicplayer.service;

import com.musicplayer.exception.SongNotFoundException;
import com.musicplayer.model.Song;
import com.musicplayer.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

     @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private Song song;

     @BeforeEach
    void setUp() {
        song = new Song();
        song.setId(1L);
        song.setTitle("Dog Eat Dog II");
        song.setArtist("Odumodublvck");
        song.setAlbum("Eziokwu");
        song.setDuration(240);
        song.setReleaseYear(2023);
    }
 @Nested
    class GetSongByIdTests {
        
        @Test
        void whenSongExists_thenReturnSong() {
            when(songRepository.findById(1L)).thenReturn(Optional.of(song));

            Song result = songService.getSongById(1L);

            assertNotNull(result);
            assertEquals(song.getId(), result.getId());
            assertEquals(song.getTitle(), result.getTitle());
            assertEquals(song.getArtist(), result.getArtist());
            assertEquals(song.getAlbum(), result.getAlbum());
            assertEquals(song.getDuration(), result.getDuration());
            assertEquals(song.getReleaseYear(), result.getReleaseYear());

            verify(songRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(songRepository);
        }

        @Test
        void whenSongNotExists_thenThrowException() {
            Long songId = 1L;
            when(songRepository.findById(songId)).thenReturn(Optional.empty());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.getSongById(songId)
            );

            assertEquals("Song with ID " + songId + " not found", exception.getMessage());

            verify(songRepository, times(1)).findById(songId);
            verifyNoMoreInteractions(songRepository);
        }
        
        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 999L})
        void whenInvalidIdProvided_thenThrowException(Long invalidId) {
            when(songRepository.findById(invalidId)).thenReturn(Optional.empty());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.getSongById(invalidId)
            );

            assertEquals("Song with ID " + invalidId + " not found", exception.getMessage());
            verify(songRepository, times(1)).findById(invalidId);
        }
    }


     @Nested
    class PaginationTests {
        
        @ParameterizedTest
        @CsvSource({
            "title, asc",
            "title, desc", 
            "artist, asc",
            "artist, desc",
            "album, asc",
            "album, desc",
            "duration, asc",
            "duration, desc",
            "releaseYear, asc",
            "releaseYear, desc",
            "id, asc",
            "id, desc"
        })
        void withVariousSortOptions_thenReturnSortedPage(String sortBy, String direction) {
            Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(0, 10, Sort.by(sortDirection, sortBy));
            Page<Song> page = new PageImpl<>(Collections.singletonList(song));

            when(songRepository.findAll(pageable)).thenReturn(page);

            Page<Song> result = songService.getSongs(0, 10, sortBy, direction);

            assertEquals(1, result.getTotalElements());
            verify(songRepository, times(1)).findAll(pageable);
        }
        
        @ParameterizedTest
        @CsvSource({
            "0, 5",
            "1, 10", 
            "2, 20",
            "0, 50"
        })
        void withVariousPageSizes_thenReturnCorrectPage(int pageNumber, int pageSize) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            Page<Song> page = new PageImpl<>(Collections.singletonList(song));

            when(songRepository.findAll(pageable)).thenReturn(page);

            Page<Song> result = songService.getSongs(pageNumber, pageSize, "title", "asc");

            assertEquals(1, result.getTotalElements());
            verify(songRepository, times(1)).findAll(pageable);
        }
        
        @Test
        void withInvalidSortDirection_thenUseDefaultAscending() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
            Page<Song> page = new PageImpl<>(Collections.singletonList(song));

            when(songRepository.findAll(pageable)).thenReturn(page);

            Page<Song> result = songService.getSongs(0, 10, "title", "invalid");

            assertEquals(1, result.getTotalElements());
            verify(songRepository, times(1)).findAll(pageable);
        }
    }
  @Nested
class SaveSongTests {

    @Test
    void whenValidSong_thenSaveSuccessfully() {
        
        when(songRepository.save(song)).thenReturn(song);

        
        Song saved = songService.saveSong(song);

       
        assertNotNull(saved);
        assertEquals(song.getId(), saved.getId());
        assertEquals("Dog Eat Dog II", saved.getTitle());
        assertEquals("Odumodublvck", saved.getArtist());
        assertEquals("Eziokwu", saved.getAlbum());
        assertEquals(240, saved.getDuration());
        assertEquals(2023, saved.getReleaseYear());

        verify(songRepository, times(1)).save(song);
        verifyNoMoreInteractions(songRepository);
    }

    @Test
    void whenSongIsNull_thenThrowIllegalArgumentException() {
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> songService.saveSong(null)
        );

        assertEquals("Song cannot be null", exception.getMessage());
        verifyNoInteractions(songRepository);
    }

    @Test
    void whenSavingSong_thenRepositorySaveIsCalled() {
        
        when(songRepository.save(any(Song.class))).thenReturn(song);

        
        Song saved = songService.saveSong(song);

        
        assertNotNull(saved);
        verify(songRepository, times(1)).save(any(Song.class));
    }

    @Test
    void whenSongSavedReturnsNull_thenReturnNull() {
       
        when(songRepository.save(song)).thenReturn(null);

      
        Song result = songService.saveSong(song);

        
        assertNull(result);
        verify(songRepository, times(1)).save(song);
    }
}


  @Nested
    class DeleteSongTests {
        
        @Test
        void whenSongExists_thenDeleteSuccessfully() {
            Long songId = 1L;
            when(songRepository.existsById(songId)).thenReturn(true);

            songService.deleteSong(songId);

            verify(songRepository, times(1)).existsById(songId);
            verify(songRepository, times(1)).deleteById(songId);
            verifyNoMoreInteractions(songRepository);
        }

        @Test
        void whenSongNotExists_thenThrowException() {
            Long songId = 1L;
            when(songRepository.existsById(songId)).thenReturn(false);

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.deleteSong(songId)
            );

            assertEquals("Song with ID " + songId + " not found", exception.getMessage());

            verify(songRepository, times(1)).existsById(songId);
            verify(songRepository, never()).deleteById(songId);
            verifyNoMoreInteractions(songRepository);
        }
        
        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 999L})
        void whenInvalidIdProvided_thenThrowException(Long invalidId) {
            when(songRepository.existsById(invalidId)).thenReturn(false);

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.deleteSong(invalidId)
            );

            assertEquals("Song with ID " + invalidId + " not found", exception.getMessage());
            verify(songRepository, times(1)).existsById(invalidId);
            verify(songRepository, never()).deleteById(any());
        }
    }

 @Nested
    class UpdateSongTests {
        
        @Test
        void whenSongExists_thenUpdateSuccessfully() {
            when(songRepository.findById(1L)).thenReturn(Optional.of(song));
            when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Song updatedSong = new Song();
            updatedSong.setTitle("Declan Rice");
            updatedSong.setArtist("Odumodublvck");
            updatedSong.setAlbum("Eziokwu");
            updatedSong.setDuration(200);
            updatedSong.setReleaseYear(2023);

            Song result = songService.updateSong(1L, updatedSong);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Declan Rice", result.getTitle());
            assertEquals("Odumodublvck", result.getArtist());
            assertEquals("Eziokwu", result.getAlbum());
            assertEquals(200, result.getDuration());
            assertEquals(2023, result.getReleaseYear());

            verify(songRepository, times(1)).findById(1L);
            verify(songRepository, times(1)).save(any(Song.class));
            verifyNoMoreInteractions(songRepository);
        }

        @Test
        void whenSongNotExists_thenThrowException() {
            Long songId = 1L;
            when(songRepository.findById(songId)).thenReturn(Optional.empty());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.updateSong(songId, song)
            );

            assertEquals("Song with ID " + songId + " not found", exception.getMessage());

            verify(songRepository, times(1)).findById(songId);
            verify(songRepository, never()).save(any(Song.class));
        }
        
        @Test
        void whenNullSong_thenThrowException() {
            assertThrows(IllegalArgumentException.class, 
                () -> songService.updateSong(1L, null));
            
            verifyNoInteractions(songRepository);
        }
        
        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, 999L})
        void whenInvalidIdProvided_thenThrowException(Long invalidId) {
            when(songRepository.findById(invalidId)).thenReturn(Optional.empty());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.updateSong(invalidId, song)
            );

            assertEquals("Song with ID " + invalidId + " not found", exception.getMessage());
            verify(songRepository, times(1)).findById(invalidId);
            verify(songRepository, never()).save(any());
        }
    }

 @Nested
    class SearchSongsByArtistTests {
        
        @Test
        void whenArtistExists_thenReturnSongs() {
            String artist = "Odumodublvck";
            when(songRepository.findByArtistContainingIgnoreCase(artist))
                    .thenReturn(Arrays.asList(song));

            List<Song> results = songService.searchSongsByArtist(artist);

            assertNotNull(results);
            assertEquals(1, results.size());

            Song resultSong = results.get(0);
            assertEquals("Dog Eat Dog II", resultSong.getTitle());
            assertEquals("Odumodublvck", resultSong.getArtist());
            assertEquals("Eziokwu", resultSong.getAlbum());
            assertEquals(240, resultSong.getDuration());
            assertEquals(2023, resultSong.getReleaseYear());

            verify(songRepository, times(1)).findByArtistContainingIgnoreCase(artist);
            verifyNoMoreInteractions(songRepository);
        }

        @Test
        void whenArtistNotExists_thenThrowException() {
            when(songRepository.findByArtistContainingIgnoreCase("Unknown"))
                    .thenReturn(Collections.emptyList());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.searchSongsByArtist("Unknown")
            );

            assertEquals("No songs found for artist: Unknown", exception.getMessage());
            verify(songRepository, times(1)).findByArtistContainingIgnoreCase("Unknown");
            verifyNoMoreInteractions(songRepository);
        }
        
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void whenBlankArtistProvided_thenThrowException(String blankArtist) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> songService.searchSongsByArtist(blankArtist)
            );

            assertEquals("Artist cannot be blank", exception.getMessage());
            verifyNoInteractions(songRepository);
        }
    }

   
   @Nested
    class SearchSongsByAlbumTests {
        
        @Test
        void whenAlbumExists_thenReturnSongs() {
            when(songRepository.findByAlbumContainingIgnoreCase("Eziokwu"))
                    .thenReturn(Arrays.asList(song));

            List<Song> results = songService.searchSongsByAlbum("Eziokwu");

            assertNotNull(results);
            assertEquals(1, results.size());

            Song foundSong = results.get(0);
            assertEquals("Eziokwu", foundSong.getAlbum());
            assertEquals("Dog Eat Dog II", foundSong.getTitle());
            assertEquals("Odumodublvck", foundSong.getArtist());
            assertEquals(240, foundSong.getDuration());
            assertEquals(2023, foundSong.getReleaseYear());

            verify(songRepository, times(1)).findByAlbumContainingIgnoreCase("Eziokwu");
        }

        @Test
        void whenAlbumNotExists_thenThrowException() {
            when(songRepository.findByAlbumContainingIgnoreCase("Unknown"))
                    .thenReturn(Collections.emptyList());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.searchSongsByAlbum("Unknown")
            );

            assertEquals("No songs found for album: Unknown", exception.getMessage());

            verify(songRepository, times(1)).findByAlbumContainingIgnoreCase("Unknown");
            verifyNoMoreInteractions(songRepository);
        }
        
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void whenBlankAlbumProvided_thenThrowException(String blankAlbum) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> songService.searchSongsByAlbum(blankAlbum)
            );

            assertEquals("Album cannot be blank", exception.getMessage());
            verifyNoInteractions(songRepository);
        }
    }
     
       @Nested
    class SearchSongsByTitleTests {
        
        @Test
        void whenTitleExists_thenReturnSongs() {
            when(songRepository.findByTitleContainingIgnoreCase("Dog Eat Dog II"))
                    .thenReturn(Arrays.asList(song));

            List<Song> results = songService.searchSongsByTitle("Dog Eat Dog II");

            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals("Dog Eat Dog II", results.get(0).getTitle());
            assertEquals("Odumodublvck", results.get(0).getArtist());
            assertEquals("Eziokwu", results.get(0).getAlbum());
            assertEquals(240, results.get(0).getDuration());
            assertEquals(2023, results.get(0).getReleaseYear());

            verify(songRepository, times(1)).findByTitleContainingIgnoreCase("Dog Eat Dog II");
        }

        @Test
        void whenTitleNotExists_thenThrowException() {
            when(songRepository.findByTitleContainingIgnoreCase("Unknown"))
                    .thenReturn(Collections.emptyList());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.searchSongsByTitle("Unknown")
            );

            
            assertEquals("No songs found with title: Unknown", exception.getMessage());

            verify(songRepository, times(1)).findByTitleContainingIgnoreCase("Unknown");
            verifyNoMoreInteractions(songRepository);
        }
        
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void whenBlankTitleProvided_thenThrowException(String blankTitle) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> songService.searchSongsByTitle(blankTitle)
            );

            assertEquals("Title cannot be blank", exception.getMessage());
            verifyNoInteractions(songRepository);
        }
    }

     @Nested
    class SearchSongsTests {
        
        @Test
        void whenAllCriteriaMatch_thenReturnSongs() {
            String title = "Dog Eat Dog II";
            String artist = "Odumodublvck";
            String album = "Eziokwu";

            when(songRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                    title, artist, album)).thenReturn(Arrays.asList(song));

            List<Song> results = songService.searchSongs(title, artist, album);

            assertNotNull(results);
            assertEquals(1, results.size());

            Song resultSong = results.get(0);
            assertEquals(title, resultSong.getTitle());
            assertEquals(artist, resultSong.getArtist());
            assertEquals(album, resultSong.getAlbum());
            assertEquals(240, resultSong.getDuration());
            assertEquals(2023, resultSong.getReleaseYear());

            verify(songRepository, times(1))
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                            title, artist, album);
            verifyNoMoreInteractions(songRepository);
        }

        @Test
        void whenNoCriteriaMatch_thenThrowException() {
            String title = "Unknown";
            String artist = "Unknown";
            String album = "Unknown";

            when(songRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                    title, artist, album)).thenReturn(Collections.emptyList());

            SongNotFoundException exception = assertThrows(
                    SongNotFoundException.class,
                    () -> songService.searchSongs(title, artist, album)
            );

            assertEquals("No songs found matching title: Unknown, artist: Unknown, album: Unknown",
                    exception.getMessage());

            verify(songRepository, times(1))
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                            title, artist, album);
            verifyNoMoreInteractions(songRepository);
        }

        @ParameterizedTest
        @CsvSource({
            "Dog, '', ''",
            "'', Odumodu, ''",
            "'', '', Eziokwu",
            "Dog, Odumodu, ''",
            "Dog, '', Eziokwu",
            "'', Odumodu, Eziokwu"
        })
        void withPartialCriteria_thenReturnMatchingSongs(String title, String artist, String album) {
            when(songRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                    title, artist, album))
                    .thenReturn(Arrays.asList(song));

            List<Song> results = songService.searchSongs(title, artist, album);

            assertNotNull(results);
            assertEquals(1, results.size());
            verify(songRepository, times(1))
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCaseAndAlbumContainingIgnoreCase(
                            title, artist, album);
        }
        
        @ParameterizedTest
@CsvSource({
    "'', '', ''",
    "' ', ' ', ' '",
    "null, null, null"
})
void whenAllCriteriaBlank_thenThrowException(String title, String artist, String album) {
    
    String normalizedTitle = "null".equals(title) ? null : title;
    String normalizedArtist = "null".equals(artist) ? null : artist;
    String normalizedAlbum = "null".equals(album) ? null : album;

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> songService.searchSongs(normalizedTitle, normalizedArtist, normalizedAlbum)
    );

    assertEquals("At least one search criteria must be provided", exception.getMessage());
    verifyNoInteractions(songRepository);
}
    }
}