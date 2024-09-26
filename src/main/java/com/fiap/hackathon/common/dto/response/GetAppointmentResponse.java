package com.fiap.hackathon.common.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Accessors(chain = true)
public class GetAppointmentResponse {

    private String id;
    private String doctorId;
    private String patientId;
    private LocalDate date;
    private String timeslot;
    private LocalDateTime createdAt;
}
