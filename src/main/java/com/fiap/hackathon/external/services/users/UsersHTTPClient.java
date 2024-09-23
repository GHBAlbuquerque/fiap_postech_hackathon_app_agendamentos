package com.fiap.hackathon.external.services.users;

import com.fiap.hackathon.external.services.users.dtos.Doctor;
import com.fiap.hackathon.external.services.users.dtos.Patient;
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
    ResponseEntity<Doctor> getDoctorById(
            @PathVariable("id") final Long customerId,
            @RequestHeader("microsservice") final String microsservico,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/doctors/{id}/timetable}")
    ResponseEntity<Patient> getDoctorTimetable(
            @PathVariable("id") final Long customerId,
            @RequestHeader("microsservice") final String microsservico,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/patients/{id}")
    ResponseEntity<Doctor> getPatientById(
            @PathVariable("id") final Long customerId,
            @RequestHeader("microsservice") final String microsservico,
            @RequestHeader("Content-Type") final String contentType
    );
}
