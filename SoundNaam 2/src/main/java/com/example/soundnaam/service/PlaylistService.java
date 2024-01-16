package com.example.soundnaam.service;


import com.example.soundnaam.POJO.Playlist;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository){
        this.repository = repository;
    }

    @Cacheable(value = "Playlist")
    @RabbitListener(queues = "AllPlaylist")
    public List<Playlist> getAllPlaylist(){
        try{
            return repository.findAll();
        }catch (Exception e){
            return null;
        }
    }
    @RabbitListener(queues = "GetPlaylistByEmail")
    public  List<Playlist> getPlaylistByEmail(String playlistEmail){
        try{
            return repository.findByEmail(playlistEmail);
        }catch (Exception e){
            throw e;
        }
    }

    @RabbitListener(queues = "GetNamePlaylist")
    public  Playlist getPlaylistByName(String playlistName){
        return repository.findByName(playlistName);
    }

    @CacheEvict(value = "Playlist", allEntries = true)
    @RabbitListener(queues = "AddPlaylist")
    public boolean addPlaylist(Playlist playlist){
        try{
            this.repository.save(playlist);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @CachePut(value = "Playlist")
    @RabbitListener(queues = "UpdatePlaylist")
    public boolean updatePlaylist(Playlist playlist){
        try{
            this.repository.save(playlist);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @CacheEvict(value = "Playlist", allEntries = true)//allEntries=>ลบข้อมูลในCacheแล้วโหลดใหม่
    @RabbitListener(queues = "DeletePlaylist")
    public boolean deletePlaylist(Playlist playlist){
        try{
            this.repository.delete(playlist);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
