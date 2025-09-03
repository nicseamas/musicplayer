package com.musicplayer.service;

import com.musicplayer.model.Song;

import com.musicplayer.repository.SongRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository){
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs(){
        return songRepository.findAll();
    }

    public Song saveSong(Song song){
        return songRepository.save(song);
    }

    public void deleteSong(Long id){
        songRepository.deleteById(id);
    }



    


    
}
