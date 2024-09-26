package com.fiap.hackathon.external.services.users;

import com.fiap.hackathon.external.services.users.dtos.DoctorDTO;
import com.fiap.hackathon.external.services.users.dtos.DoctorTimetableDTO;
import com.fiap.hackathon.external.services.users.dtos.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "msusuario",
        url = "${fiap.postech.hackathon.msusuario.url}"
)
public interface UsersHTTPClient {

    @GetMapping(value = "/doctors/{id}")
    ResponseEntity<DoctorDTO> getDoctorById(
            @PathVariable("id") final String doctorId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/doctors/{id}/timetable")
    ResponseEntity<DoctorTimetableDTO> getDoctorTimetable(
            @PathVariable("id") final String doctorId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/patients/{id}")
    ResponseEntity<PatientDTO> getPatientById(
            @PathVariable("id") final String patientId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );
}
