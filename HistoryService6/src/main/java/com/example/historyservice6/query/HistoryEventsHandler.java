package com.example.historyservice6.query;

import com.example.historyservice6.core.data.HistoryEntity;
import com.example.historyservice6.core.data.HistoryRepository;
import com.example.historyservice6.core.event.HistoryCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class HistoryEventsHandler {
    private final HistoryRepository repository;
    public HistoryEventsHandler(HistoryRepository repository){
        this.repository = repository;
    }

    @EventHandler
    public void on(HistoryCreatedEvent event) {
        HistoryEntity historyEntity = new HistoryEntity();
        BeanUtils.copyProperties(event, historyEntity);
        repository.save(historyEntity);
    }
}
