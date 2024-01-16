package com.example.membership.query.rest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSubscriptionRestModel {
    private String useremail;
    private String artistname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean subscribed;

}
