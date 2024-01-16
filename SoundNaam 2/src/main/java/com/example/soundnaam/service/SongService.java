package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Playlist;
import com.example.soundnaam.POJO.Song;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    @Autowired
    private SongRepository repository;

    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "GetAllSongQueue")
    public List<Song> getAllSong(){
        System.out.println("get all song class");
        try {
            System.out.println("get all song try");
            return repository.findAll();
        }catch (Exception e){
            System.out.println("get all song catch");
            System.out.println(e);
            return null;
        }
    }

    @RabbitListener(queues = "GetSongByAlbumQueue")
    public List<Song> getSongByAlbum(String album){
        try {
            return repository.findByAlbum(album);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @RabbitListener(queues = "GetSongById")
    public List<Song> getSongById(String id){
        try {
            return repository.findAllById(Collections.singleton(id));
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @RabbitListener(queues = "AddSongQueue")
    public boolean addSong(@RequestBody Song song){
//        public boolean addSong(Song song){
        try {
            repository.insert(song);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @RabbitListener(queues = "DeleteSongQueue")
    public boolean deleteSong(Song song){
        try {
            repository.delete(song);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @RabbitListener(queues = "UpdateSongQueue")
    public boolean updateSong(Song song){
        try {
            repository.save(song);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }
    @RabbitListener(queues = "GetSongByNameQueue")
    public Song findByName(String title){
        try{
            return repository.findByName(title);
        }catch (Exception e){
            System.out.println(e);
            throw e;
        }

    }
    @RabbitListener(queues = "GetSongByEmailQueue")
    public  List<Song> findByEmail(String email){
        try{

            System.out.println(email);
            return repository.findByEmail(email);
        }catch (Exception e){
            throw e;
        }
    }

    @RabbitListener(queues = "GetSongByArtistQueue")
    public  List<Song> findByArtist(String artist){
        try{
            return repository.findByEmail(artist);
        }catch (Exception e){
            throw e;
        }
    }
}