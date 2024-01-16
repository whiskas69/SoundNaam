package com.example.soundnaam.controller;

import com.example.soundnaam.POJO.ContentMembership;
import com.example.soundnaam.POJO.Song;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ContentMembershipController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ContentMembershipController() {
    }

    @RequestMapping(value = "/getContent", method = GET)
    public List<ContentMembership> getAllContent(){
        return (List<ContentMembership>) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "getAll", "");
    }
    @RequestMapping(value = "/addContent", method = POST)
    public boolean addContent(@RequestBody ContentMembership content){
        return (boolean) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "add", content);
    }

    @RequestMapping(value ="/delContent", method = POST)
    public boolean delContent(@RequestBody ContentMembership content){
        return (boolean) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "delete", content);
    }
    @RequestMapping(value ="/updateContent", method = POST)
    public boolean updateContent(@RequestBody ContentMembership content){
        return (boolean) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "update", content);
    }
    @RequestMapping(value = "/getContentByArtist/{artist}", method= GET)
    public List<ContentMembership> getContentByArtist(@PathVariable String artist){
        return (List<ContentMembership>) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "artist", artist);
    }

    @RequestMapping(value = "/getContentByEmail/{email}", method= GET)
    public List<ContentMembership> getContentByEmail(@PathVariable String email){
        return (List<ContentMembership>) rabbitTemplate.convertSendAndReceive("ContentMemExchange", "email", email);
    }






}
