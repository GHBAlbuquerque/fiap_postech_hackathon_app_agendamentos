package com.fiap.hackathon.common.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateAppointmentRequest {

    private String doctorId;
    private String patientId;
    private LocalDate date;
    private String timeslot;
}
