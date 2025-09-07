package com.musicplayer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


@Data
@Entity

public class Song {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 

  private Long id;
  
@NotBlank(message = "Song title is required")
  private String title;

  @NotBlank(message = "Artist name is required")
  private String artist;

  private String album; 

 
 @Positive(message = "Duration must be greater than zero")
  private int duration;

 @Min(value = 1877, message = "Release year cannot be before 1877 ")
 @Max(value = 3000, message = "Release year cannot be after 3000")
  private int releaseYear;
}
