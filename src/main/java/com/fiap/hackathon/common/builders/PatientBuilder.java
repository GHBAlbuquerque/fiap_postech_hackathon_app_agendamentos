package com.fiap.hackathon.common.builders;

import com.fiap.hackathon.core.entity.Patient;
import com.fiap.hackathon.external.services.users.dtos.PatientDTO;

public class PatientBuilder {

    public static Patient fromDTOtoDomain(PatientDTO dto) {
        return new Patient()
                .setName(dto.getName())
                .setBirthday(dto.getBirthday())
                .setCpf(dto.getCpf())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setContactNumber(dto.getContactNumber());
    }
}
