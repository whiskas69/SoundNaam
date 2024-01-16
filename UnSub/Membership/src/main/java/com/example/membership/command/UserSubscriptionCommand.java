package com.example.membership.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSubscriptionCommand {
    @TargetAggregateIdentifier
    private final String subscriptionId;
    private final String useremail;
    private final String artistname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final boolean subscribed;
}
