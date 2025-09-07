package com.musicplayer.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Song createValidSong() {
        Song song = new Song();
        song.setTitle("Dog Eat Dog II");
        song.setArtist("Odumodublvck");
        song.setAlbum("Eziokwu");
        song.setDuration(240);
        song.setReleaseYear(2023);
        return song;
    }

    @Test
    @DisplayName("Should pass validation for valid song")
    void testValidSong() {
        Song song = createValidSong();
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue(violations.isEmpty(), "Valid song should not produce violations");
    }

    @Test
    @DisplayName("Should fail when title is blank")
    void testBlankTitle() {
        Song song = createValidSong();
        song.setTitle("");

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertFalse(violations.isEmpty());
        assertEquals("Song title is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail when artist is blank")
    void testBlankArtist() {
        Song song = createValidSong();
        song.setArtist("");

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertFalse(violations.isEmpty());
        assertEquals("Artist name is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail when duration is zero or negative")
    void testInvalidDuration() {
        Song song = createValidSong();
        song.setDuration(0);

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertFalse(violations.isEmpty());
        assertEquals("Duration must be greater than zero", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail when release year is before 1877")
    void testReleaseYearTooOld() {
        Song song = createValidSong();
        song.setReleaseYear(1500);

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail when release year is in the future")
    void testReleaseYearFuture() {
        Song song = createValidSong();
        song.setReleaseYear(3000);

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertFalse(violations.isEmpty());
 }
}