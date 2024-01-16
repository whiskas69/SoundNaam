package com.example.historyservice6.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
public class CreateHistoryCommand {
    @TargetAggregateIdentifier
    private String _id;
    private int historyId;
    private int userId;
    private int songId;
    private String songName;
    private String email;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;
}
