package com.example.soundnaam.controller;

import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Podcast;
import com.example.soundnaam.service.AudioService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
public class PodcastController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AudioService audioService;
    //    private Song model = new Song( "asdf", "asdf", "asdf", "asdf", "asdf", "asdf", 12, 12, 12, "".getBytes());
//    private Song modelUpdate = new Song("updated", "susccess", "asdf", "asdf", "asdf", "asdf", 12, 12, 12, "".getBytes());

    public PodcastController() {
    }

//    @RequestMapping(value = "/", method = GET)
//    public String test(){
//        return "pass";
//    }

    @GetMapping("/getAudioPodcast/{id}")
    public ResponseEntity<byte[]> streamAudio(@PathVariable String id) {
        Optional<Music> audioOptional = audioService.getAudioById(id);

        if (audioOptional.isPresent()) {
            Music audio = audioOptional.get();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(audio.getMimeType()))
                    .body(audio.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @RequestMapping(value = "/getSeries/{series}", method = GET)
    public List<Podcast> getAlbum(@PathVariable String series){
        return (List<Podcast>) rabbitTemplate.convertSendAndReceive("PodcastExchange", "getAlbum", series);
    }

    @RequestMapping(value = "/getPodcast", method = GET)
    public List<Podcast> getAllPodcast(){
        return (List<Podcast>) rabbitTemplate.convertSendAndReceive("PodcastExchange", "getAll", "");
    }
    @RequestMapping(value ="/delPodcast", method = POST)
    public boolean delPodcast(@RequestBody Podcast podcast){
        return (boolean) rabbitTemplate.convertSendAndReceive("PodcastExchange", "delete", podcast);
    }

    @RequestMapping(value = "/updatePodcast", method = POST)
    public boolean updatePodcast(@RequestBody Podcast modelUpdate){
//    public boolean updateSong(){
//        validation id if not have id will return false
//        cuz use save so if no have id will generate new data
        return (boolean) rabbitTemplate.convertSendAndReceive("SongExchange", "update",  modelUpdate);
    }

    @RequestMapping(value = "/addPodcast", method = POST)
    public boolean addPodcast(@RequestBody Podcast podcast){
//    public boolean addSong(){
        System.out.println("pass");
        return (boolean) rabbitTemplate.convertSendAndReceive("PodcastExchange", "add", podcast);
    }
    @RequestMapping(value = "/getPodcastById/{id}", method = GET)
    public List<Podcast> getPodcastById(@PathVariable String id){
        return (List<Podcast>) rabbitTemplate.convertSendAndReceive("PodcastExchange", "getByid", id);
    }
}