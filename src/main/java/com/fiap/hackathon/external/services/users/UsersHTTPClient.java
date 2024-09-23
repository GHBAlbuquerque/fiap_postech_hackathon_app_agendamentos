package com.fiap.hackathon.external.services.users;

import com.fiap.hackathon.external.services.users.dtos.Doctor;
import com.fiap.hackathon.external.services.users.dtos.DoctorTimetable;
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
            @PathVariable("id") final String doctorId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/doctors/{id}/timetable}")
    ResponseEntity<DoctorTimetable> getDoctorTimetable(
            @PathVariable("id") final String doctorId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/patients/{id}")
    ResponseEntity<Patient> getPatientById(
            @PathVariable("id") final String patientId,
            @RequestHeader("microsservice") final String microsservice,
            @RequestHeader("Content-Type") final String contentType
    );
}
