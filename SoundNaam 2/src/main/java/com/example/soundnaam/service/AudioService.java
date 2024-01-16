package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AudioService {
    @Autowired
    private AudioRepository repository;

    public AudioService() {
    }

    public AudioService(AudioRepository repository) {
        this.repository = repository;
    }


    public Optional<Music> getAudioById(String id) {
        Optional<Music> byId = repository.findById(id);
        return byId;
    }
//    @RabbitListener(queues = "GetAudioById")
//    public Music getAllSong(String id){
//        try {
//            Optional<Music> musicOptional = audioRepository.findById(id);
//            return musicOptional.orElse(null);
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    }

}
