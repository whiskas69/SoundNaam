package com.example.membership.command.rest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscribeUserRestModel {
    private String useremail;
    private String artistname;
    private String code;
}
