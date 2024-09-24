package com.fiap.hackathon.external.services.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTimetableDTO {

    private String id;
    private String doctorId;
    private Set<String> sunday;
    private Set<String> monday;
    private Set<String> tuesday;
    private Set<String> wednesday;
    private Set<String> thursday;
    private Set<String> friday;
    private Set<String> saturday;
}
