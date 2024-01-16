package com.example.soundnaam.controller;


import com.example.soundnaam.POJO.Playlist;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaylistController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/getAllPlaylist", method = RequestMethod.GET)
    public List<Playlist> getAllPlaylist(){
        System.out.println("111111");
        return (List<Playlist>) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "getall", "");
    }
    @RequestMapping(value = "/getPlaylistByEmail/{email}", method = RequestMethod.GET)
    public List<Playlist> getPlaylistByEmail(@PathVariable String email){
        System.out.println("11111133333333");
        return (List<Playlist>) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "email", email);
    }

    @RequestMapping(value = "/getNamePlaylist/{playlistName}", method = RequestMethod.GET)
    public Playlist getNamePlaylist(@PathVariable String playlistName){

        return (Playlist) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "name", playlistName);
    }

    @RequestMapping(value = "/addPlaylist", method = RequestMethod.POST)
    public boolean addProduct(@RequestBody Playlist playlist){
        System.out.println("47888888888888888");
        return (boolean) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "add", playlist);
    }

    @RequestMapping(value = "/updatePlaylist", method = RequestMethod.POST)
    public boolean updateProduct(@RequestBody Playlist playlist){
        return (boolean) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "update", playlist);
    }
    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public boolean deleteProduct(@RequestBody Playlist playlist){
        return (boolean) rabbitTemplate.convertSendAndReceive("PlaylistExchange", "delete", playlist);
    }
}