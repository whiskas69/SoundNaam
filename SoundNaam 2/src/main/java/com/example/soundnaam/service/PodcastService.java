package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Podcast;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PodcastService {
    @Autowired
    private PodcastRepository repository;

    public PodcastService(PodcastRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "GetAllPodcastQueue")
    public List<Podcast> getAllPodcast(){
//        System.out.println("get all song class");
        try {
//            System.out.println("get all song try");
            return repository.findAll();
        }catch (Exception e){
//            System.out.println("get all song catch");
            System.out.println(e);
            return null;
        }
    }

//    @RabbitListener(queues = "GetPodcastBySeriesQueue")
//    public List<Podcast> getPodcastBySeries(String series){
//        try {
//            return repository.findBySeries(series);
//        }catch (Exception e){
//            System.out.println(e);
//            return null;
//        }
//    }



    @RabbitListener(queues = "AddPodcastQueue")
    public boolean addPodcast(@RequestBody Podcast podcast){
//        public boolean addSong(Song song){
        try {
            repository.insert(podcast);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @RabbitListener(queues = "DeletePodcastQueue")
    public boolean deletePodcast(Podcast podcast){
        try {
            repository.delete(podcast);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @RabbitListener(queues = "UpdatePodcastQueue")
    public boolean updatePodcast(Podcast podcast){
        try {
            repository.save(podcast);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

}
