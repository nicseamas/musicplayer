package com.musicplayer.repository;

import com.musicplayer.model.Song;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

public interface SongRepository extends JpaRepository<Song,Long> {


    
}
