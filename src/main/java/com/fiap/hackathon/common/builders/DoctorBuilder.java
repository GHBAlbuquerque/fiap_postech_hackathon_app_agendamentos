package com.fiap.hackathon.common.builders;

import com.fiap.hackathon.core.entity.Doctor;
import com.fiap.hackathon.external.services.users.dtos.DoctorDTO;

public class DoctorBuilder {

    public static Doctor fromDTOtoDomain(DoctorDTO dto) {
        return new Doctor()
                .setCrm(dto.getCrm())
                .setMedicalSpecialty(dto.getMedicalSpecialty())
                .setName(dto.getName())
                .setBirthday(dto.getBirthday())
                .setCpf(dto.getCpf())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setContactNumber(dto.getContactNumber());
    }
}
