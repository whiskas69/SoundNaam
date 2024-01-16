package com.example.historyservice6.query.rest;

import com.example.historyservice6.query.FindHistoryEmailQuery;
import com.example.historyservice6.query.FindHistoryQuery;
import com.example.historyservice6.query.FindHistorySongNameQuery;
import com.example.historyservice6.query.FindHistorySongNameQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gethistory")
public class HistoryQueryController {
    private final QueryGateway queryGateway;

    public HistoryQueryController(QueryGateway queryGateway){
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<HistoryRestModel> getHistory() {
        FindHistoryQuery findHistoryQuery = new FindHistoryQuery();
        return queryGateway.query(
                findHistoryQuery,
                ResponseTypes.multipleInstancesOf(HistoryRestModel.class)
        ).join();
    }

    @GetMapping("/{email}")
    public List<HistoryRestModel> getHistoryEmail(@PathVariable String email){
        FindHistoryEmailQuery findHistoryEmailQuery = new FindHistoryEmailQuery();
        findHistoryEmailQuery.setEmail(email);
        return queryGateway.query(
                findHistoryEmailQuery,
                ResponseTypes.multipleInstancesOf(HistoryRestModel.class)
        ).join();
    }

    @GetMapping("/song/{songName}")
    public List<HistoryRestModel> getHistorySongName(@PathVariable String songName){
        FindHistorySongNameQuery findHistorySongNameQuery = new FindHistorySongNameQuery();
        findHistorySongNameQuery.setSongName(songName);
        return queryGateway.query(
                findHistorySongNameQuery,
                ResponseTypes.multipleInstancesOf(HistoryRestModel.class)
        ).join();
    }
}
