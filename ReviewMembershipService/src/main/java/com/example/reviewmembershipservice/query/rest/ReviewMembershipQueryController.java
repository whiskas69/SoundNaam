package com.example.reviewmembershipservice.query.rest;

import com.example.reviewmembershipservice.query.FindReviewEachMembershipQuery;
import com.example.reviewmembershipservice.query.FindReviewMembershipQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviewmembership")
public class ReviewMembershipQueryController {
    private final QueryGateway queryGateway;

    public ReviewMembershipQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ReviewMembershipRestModel> getReviewMemberships() {
        FindReviewMembershipQuery findReviewSongsQuery = new FindReviewMembershipQuery();
        return queryGateway.query(
                findReviewSongsQuery,
                ResponseTypes.multipleInstancesOf(ReviewMembershipRestModel.class)
        ).join();
    }

    @GetMapping("/{postId}")
    public List<ReviewMembershipRestModel> getReviewEachMembership(@PathVariable String postId) {
        FindReviewEachMembershipQuery findReviewEachMembershipQuery = new FindReviewEachMembershipQuery();
        findReviewEachMembershipQuery.setPostId(postId);
        return queryGateway.query(
                findReviewEachMembershipQuery,
                ResponseTypes.multipleInstancesOf(ReviewMembershipRestModel.class)
        ).join();
    }
}
