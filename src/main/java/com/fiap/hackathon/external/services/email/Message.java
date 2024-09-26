package com.fiap.hackathon.external.services.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Message {

    String to;

    String from;

    String subject;

    String text;


}
