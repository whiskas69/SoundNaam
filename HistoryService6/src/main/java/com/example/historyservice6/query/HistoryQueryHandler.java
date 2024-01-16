package com.example.historyservice6.query;

import com.example.historyservice6.core.data.HistoryEntity;
import com.example.historyservice6.core.data.HistoryRepository;
import com.example.historyservice6.query.rest.HistoryRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryQueryHandler {
    private final HistoryRepository repository;
    public HistoryQueryHandler(HistoryRepository repository){
        this.repository = repository;
    }

    @QueryHandler
    public List<HistoryRestModel> findHistory(FindHistoryQuery query){
        List<HistoryRestModel> historyRestModels = new ArrayList<>();
        List<HistoryEntity> storeHistory = repository.findAll();
        for (HistoryEntity historyEntity : storeHistory){
            HistoryRestModel restModel = new HistoryRestModel();
            BeanUtils.copyProperties(historyEntity, restModel);
            historyRestModels.add(restModel);
        }
        return historyRestModels;
    }

    @QueryHandler
    public List<HistoryRestModel> findHistoryEmail(FindHistoryEmailQuery query){
        List<HistoryRestModel> historyRestModels = new ArrayList<>();
        List<HistoryEntity> storeHistory = repository.findByEmail(query.getEmail());
        for (HistoryEntity historyEntity : storeHistory){
            HistoryRestModel restModel = new HistoryRestModel();
            BeanUtils.copyProperties(historyEntity, restModel);
            historyRestModels.add(restModel);
        }
        return historyRestModels;
    }

    @QueryHandler
    public List<HistoryRestModel> findHistorySongname(FindHistorySongNameQuery query){
        List<HistoryRestModel> historyRestModels = new ArrayList<>();
        List<HistoryEntity> storeHistory = repository.findBySongName(query.getSongName());
        for (HistoryEntity historyEntity : storeHistory){
            HistoryRestModel restModel = new HistoryRestModel();
            BeanUtils.copyProperties(historyEntity, restModel);
            historyRestModels.add(restModel);
        }
        return historyRestModels;
    }
}
