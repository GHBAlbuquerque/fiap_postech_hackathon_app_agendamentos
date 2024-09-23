package com.fiap.hackathon.common.interfaces.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.core.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentGateway {
    
    Appointment create(Appointment appointment);

    Appointment getAppointmentById(String id);

    List<Appointment> getAppointmentsByPatient(String patientId);

    List<Appointment> getAppointmentsByDoctor(String doctorId);

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date);
    
    Boolean isDoctorAvailable(String doctorId, Appointment appointment);

    Boolean isScheduleAvailable(Appointment appointment) throws AppointmentConflictException;

}
