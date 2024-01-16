package com.example.soundnaam.controller;

import com.example.soundnaam.POJO.Artist;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArtistController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/getAllArtist", method = RequestMethod.GET)
    public List<Artist> getAllArtist(){
        return (List<Artist>) rabbitTemplate.convertSendAndReceive("ArtistExchange", "getall", "");
    }

    @RequestMapping(value = "/getArtistByName/{name}", method = RequestMethod.GET)
    public Artist getArtistByName(@PathVariable String name){
        return (Artist) rabbitTemplate.convertSendAndReceive("ArtistExchange", "getname", name);
    }

    @RequestMapping(value = "/addArtist", method = RequestMethod.POST)
    public boolean addArtist(@RequestBody Artist artist){
        return (boolean) rabbitTemplate.convertSendAndReceive("ArtistExchange", "add", artist);
    }

    @RequestMapping(value = "/updateArtist", method = RequestMethod.POST)
    public boolean updateArtist(@RequestBody Artist artist){
        return (boolean) rabbitTemplate.convertSendAndReceive("ArtistExchange", "update", artist);
    }
    @RequestMapping(value = "/deleteArtist", method = RequestMethod.POST)
    public boolean deleteArtist(@RequestBody Artist artist){
        return (boolean) rabbitTemplate.convertSendAndReceive("ArtistExchange", "delete", artist);
    }
}