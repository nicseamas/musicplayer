package com.musicplayer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class Song {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 

  private Long id; 
  
  private String title;
  private String artist;
  private String album;
  private int duration;
  private int releaseYear;
}
