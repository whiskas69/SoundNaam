package com.example.reviewpodcastservice.command.rest;

import com.example.reviewpodcastservice.command.CreateReviewPodcastCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/reviewpodcast")
public class ReviewPodcastCommandController {
    private final CommandGateway commandGateway;
    private final Environment env;

    @Autowired
    public ReviewPodcastCommandController(Environment env, CommandGateway commandGateway) {

        this.env = env;
        this.commandGateway = commandGateway;
    }
/*
    (consumes = "application/x-www-form-urlencoded")
*/

    @PostMapping
    public String createReviewPodcast(@ModelAttribute CreateReviewPodcastRestModel model) {
        CreateReviewPodcastCommand eachreview = CreateReviewPodcastCommand.builder()
                ._id(UUID.randomUUID().toString())
                .userId(model.getUserId())
                .email(model.getEmail())
                .review(model.getReview())
                .like(model.getLike())
                .date(new Date())
                .podcastId(model.getPodcastId())
                .reply(model.getReply())
                .build();
        String result;
        try {
            result = commandGateway.sendAndWait(eachreview);
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }
}
