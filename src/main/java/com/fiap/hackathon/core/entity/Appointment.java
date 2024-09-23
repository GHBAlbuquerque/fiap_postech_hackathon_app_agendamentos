package com.fiap.hackathon.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Appointment {

    private String id;
    private String doctorId;
    private String patientId;
    private LocalDate date;
    private String timeslot;
    private LocalDateTime createdAt;

}
