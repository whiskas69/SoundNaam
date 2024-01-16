package com.example.reviewsongservice.command.rest;

import com.example.reviewsongservice.command.CreateReviewCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/reviewsong")
public class ReviewSongCommandController {
    private final CommandGateway commandGateway;

    private final Environment env;

    @Autowired
    public ReviewSongCommandController(Environment env, CommandGateway commandGateway) {

        this.env = env;
        this.commandGateway = commandGateway;
    }
/*
    (consumes = "application/x-www-form-urlencoded")
*/

    @PostMapping
    public String createReview(@ModelAttribute CreateReviewRestModel model) {
        CreateReviewCommand eachreview = CreateReviewCommand.builder()
                ._id(UUID.randomUUID().toString())
                .userId(model.getUserId())
                .email(model.getEmail())
                .review(model.getReview())
                .like(model.getLike())
                .date(new Date())
                .songId(model.getSongId())
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
