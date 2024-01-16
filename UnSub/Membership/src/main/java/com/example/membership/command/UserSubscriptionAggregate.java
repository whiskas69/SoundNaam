package com.example.membership.command;

import com.example.membership.core.events.UserSubscribedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class UserSubscriptionAggregate {
    @AggregateIdentifier
    private String subscriptionId;
    private String useremail;
    private String artistname;
    private boolean subscribed;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public UserSubscriptionAggregate() {
    }



    @CommandHandler
    public UserSubscriptionAggregate(UserSubscriptionCommand userSubscriptionCommand) {
        apply(new UserSubscribedEvent(userSubscriptionCommand.getSubscriptionId(),
                    userSubscriptionCommand.getUseremail(),
                    userSubscriptionCommand.getArtistname(),
                    userSubscriptionCommand.isSubscribed(),
                    userSubscriptionCommand.getStartDate(),
                    userSubscriptionCommand.getEndDate())
            );
    }

    @EventSourcingHandler
    public void on(UserSubscribedEvent userSubscribedEvent){
        System.out.println("ON User AGGREGATE");
        System.out.println("UserSubscriptionAggregate EventSourcingHandler" + userSubscribedEvent.getStartDate());
        this.useremail = userSubscribedEvent.getArtistname();
        this.artistname = userSubscribedEvent.getUseremail();
        this.subscriptionId = userSubscribedEvent.getSubscriptionId();
        this.subscribed = userSubscribedEvent.isSubscribed();
        this.startDate = userSubscribedEvent.getStartDate();
        this.endDate = userSubscribedEvent.getEndDate();
    }
}
