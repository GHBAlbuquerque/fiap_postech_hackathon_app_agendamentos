package com.fiap.hackathon.external.services.users.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class DoctorDTO {

    private String id;
    private Boolean isActive;
    private String name;
    private LocalDate birthday;
    private String cpf;
    private String email;
    private String password;
    private String contactNumber;
    private String crm;
    private String medicalSpecialty;


}
