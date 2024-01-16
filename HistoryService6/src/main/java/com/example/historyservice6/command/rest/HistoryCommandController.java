package com.example.historyservice6.command.rest;

import com.example.historyservice6.command.CreateHistoryCommand;
import com.example.historyservice6.core.data.HistoryEntity;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/addhistory")
public class HistoryCommandController {
    private final CommandGateway commandGateway;
    private final Environment env;
//    @Autowired
//    private HistoryService historyService;
    @Autowired
    public HistoryCommandController(Environment env, CommandGateway commandGateway){
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createHistory(@ModelAttribute CreateHistoryRestModel model) {
        System.out.println("Historycommandcontroller");
        System.out.println(new Date());
        CreateHistoryCommand history = CreateHistoryCommand.builder()
                ._id(UUID.randomUUID().toString())
                .userId(model.getUserId())
                .email(model.getEmail())
                .historyId(model.getHistoryId())
                .songId(model.getSongId())
                .songName(model.getSongName())
                .timestamp(new Date())
                .build();
        String result;
        try {
            result = commandGateway.sendAndWait(history);
            System.out.println(result);
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }

//    @GetMapping("allHistory")
//    public ResponseEntity getAllHistory() {
//        return new ResponseEntity(historyService.allHistory(), HttpStatus.OK);
//    }

//    @GetMapping("history/{userId}")
//    public ResponseEntity getHistoryById(@PathVariable("userId") int userId) {
//        return new ResponseEntity(historyService.historyByUserId(userId), HttpStatus.OK);
//    }
//    @GetMapping("history/{email}")
//    public ResponseEntity getHistoryByEmail(@PathVariable("email") String email) {
//        return new ResponseEntity(historyService.historyByEmail(email), HttpStatus.OK);
//    }

//    @PostMapping("addHistory")
//    public ResponseEntity addHistory(@RequestBody HistoryEntity history) {
//        if (historyService.addHistory(history)) {
//            return new ResponseEntity("Add successfully", HttpStatus.OK);
//        }
//        return new ResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
