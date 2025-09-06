package com.musicplayer.repository;

import com.musicplayer.model.Song;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


public interface SongRepository extends JpaRepository<Song,Long> {

     List<Song> findByArtistContainingIgnoreCase(String artist);
    List<Song> findByAlbumContainingIgnoreCase(String album);
    List<Song> findByTitleContainingIgnoreCase(String title); 


    
}
