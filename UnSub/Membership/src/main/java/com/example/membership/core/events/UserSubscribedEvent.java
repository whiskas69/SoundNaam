package com.example.membership.core.events;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSubscribedEvent {
    private String subscriptionId;
    private String useremail;
    private String artistname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean subscribed;

    public UserSubscribedEvent(String subscriptionId, String useremail, String artistname, boolean subscribed, LocalDateTime startDate, LocalDateTime endDate) {
        this.subscriptionId = subscriptionId;
        this.useremail = useremail;
        this.artistname = artistname;
        this.subscribed = subscribed;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
