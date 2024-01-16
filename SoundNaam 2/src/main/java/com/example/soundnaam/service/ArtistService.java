package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Artist;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository repository;

    public ArtistService(ArtistRepository repository){
        this.repository = repository;
    }

    @Cacheable(value = "Artist")
    @RabbitListener(queues = "GetAllArtistQueue")
    public List<Artist> getAllArtist(){
        try{
            return repository.findAll();
        }catch (Exception e){
            return null;
        }
    }

    @RabbitListener(queues = "GetNameArtistQueue")
    public Artist getArtistByName(String name){
        return repository.findByName(name);
    }

    @CacheEvict(value = "Artist", allEntries = true)
    @RabbitListener(queues = "AddArtistQueue")
    public boolean addArtist(Artist artist){
        try{
            this.repository.save(artist);
            System.out.println(artist);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @CachePut(value = "Artist")
    @RabbitListener(queues = "UpdateArtistQueue")
    public boolean updateArtist(Artist artist){
        try{
            this.repository.save(artist);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @CacheEvict(value = "Artist", allEntries = true)//allEntries=>ลบข้อมูลในCacheแล้วโหลดใหม่
    @RabbitListener(queues = "DeleteArtistQueue")
    public boolean deleteArtist(Artist artist){
        try{
            this.repository.delete(artist);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}