package com.example.soundnaam.controller;

import com.example.soundnaam.service.AudioService;
import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Song;
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
public class SongController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AudioService audioService;
//    private Song model = new Song( "asdf", "asdf", "asdf", "asdf", "asdf", "asdf", 12, 12, 12, "".getBytes());
//    private Song modelUpdate = new Song("updated", "susccess", "asdf", "asdf", "asdf", "asdf", 12, 12, 12, "".getBytes());

    public SongController() {
    }

//    @RequestMapping(value = "/", method = GET)
//    public String test(){
//        return "pass";
//    }

    @GetMapping("/getAudioSong/{id}")
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


    @RequestMapping(value = "/getAlbum/{album}", method = GET)
    public List<Song> getAlbum(@PathVariable String album){
        return (List<Song>) rabbitTemplate.convertSendAndReceive("SongExchange", "getAlbum", album);
    }

    @RequestMapping(value = "/getSong", method = GET)
    public List<Song> getAllSong(){
        return (List<Song>) rabbitTemplate.convertSendAndReceive("SongExchange", "getAll", "");
    }
    @RequestMapping(value ="/delSong", method = POST)
    public boolean delSong(@RequestBody Song song){
        return (boolean) rabbitTemplate.convertSendAndReceive("SongExchange", "delete", song);
    }

    @RequestMapping(value = "/getSongByArtist/{artist}", method= GET)
    public List<Song> getSongByArtist(@PathVariable String artist){
        return (List<Song>) rabbitTemplate.convertSendAndReceive("SongExchange", "artist", artist);
    }

    @RequestMapping(value = "/getSongByName/{title}", method= GET)
    public Song getSongByName(@PathVariable String title){
        return (Song) rabbitTemplate.convertSendAndReceive("SongExchange", "title", title);
    }

    @RequestMapping(value = "/getSongByEmail/{email}", method= GET)
    public List<Song> getSongByEmail(@PathVariable String email){
        return (List<Song>) rabbitTemplate.convertSendAndReceive("SongExchange", "email", email);
    }

    @RequestMapping(value = "/updateSong", method = POST)
    public boolean updateSong(@RequestBody Song song){
//    public boolean updateSong(){
//        validation id if not have id will return false
//        cuz use save so if no have id will generate new data
        return (boolean) rabbitTemplate.convertSendAndReceive("SongExchange", "update",  song);
    }

    @RequestMapping(value = "/addSong", method = POST)
    public boolean addSong(@RequestBody Song song){
//    public boolean addSong(){
        System.out.println("pass");

        System.out.println(song);
        return (boolean) rabbitTemplate.convertSendAndReceive("SongExchange", "add", song);
    }

    @RequestMapping(value = "/getSongById/{id}", method = GET)
    public List<Song> getSongById(@PathVariable String id){
        return (List<Song>) rabbitTemplate.convertSendAndReceive("SongExchange", "getByid", id);
    }
}