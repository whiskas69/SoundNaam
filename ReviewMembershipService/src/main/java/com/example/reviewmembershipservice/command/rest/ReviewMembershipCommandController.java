package com.example.reviewmembershipservice.command.rest;

import com.example.reviewmembershipservice.command.CreateReviewMembershipCommand;
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
@RequestMapping("/reviewmembership")
public class ReviewMembershipCommandController {
    private final CommandGateway commandGateway;
    private final Environment env;

    @Autowired
    public ReviewMembershipCommandController(Environment env, CommandGateway commandGateway) {

        this.env = env;
        this.commandGateway = commandGateway;
    }
/*
    (consumes = "application/x-www-form-urlencoded")
*/

    @PostMapping
    public String createReviewMembership(@ModelAttribute CreateReviewMembershipRestModel model) {
        CreateReviewMembershipCommand eachreview = CreateReviewMembershipCommand.builder()
                ._id(UUID.randomUUID().toString())
                .userId(model.getUserId())
                .email(model.getEmail())
                .artist(model.getArtist())
                .review(model.getReview())
                .like(model.getLike())
                .date(new Date())
                .postId(model.getPostId())
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
