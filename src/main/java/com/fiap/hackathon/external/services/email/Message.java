package com.fiap.hackathon.external.services.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Message {

    private String to;

    private String from;

    private String subject;

    private String text;


}
