package com.example.historyservice6.command;

import com.example.historyservice6.core.data.HistoryRepository;
import com.example.historyservice6.core.event.HistoryCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Aggregate
public class HistoryAggregate {
    @AggregateIdentifier
    private String _id;
    private int historyId;
    private int userId;
    private int songId;
    private String songName;
    private String email;
    private Date timestamp;
    private HistoryRepository repository;

    public HistoryAggregate() {

    }

    @CommandHandler
    public HistoryAggregate(CreateHistoryCommand createHistoryCommand) {
//        if (createHistoryCommand.getEmail() == null || createHistoryCommand.getEmail().isBlank())
//    throw new IllegalArgumentException("Please write a comment");
//    }
        HistoryCreatedEvent historyCreatedEvent = new HistoryCreatedEvent();
        BeanUtils.copyProperties(createHistoryCommand ,historyCreatedEvent);
        AggregateLifecycle.apply(historyCreatedEvent);
    }

    @EventHandler
    public void on(HistoryCreatedEvent historyCreatedEvent){
        System.out.println("On aggregate");
        System.out.println(historyCreatedEvent);
        this._id = historyCreatedEvent.get_id();
        this.historyId = historyCreatedEvent.getHistoryId();
        this.userId = historyCreatedEvent.getUserId();
        this.songId = historyCreatedEvent.getSongId();
        this.songName = historyCreatedEvent.getSongName();
        this.email = historyCreatedEvent.getEmail();
        this.timestamp = historyCreatedEvent.getTimestamp();
    }
}
