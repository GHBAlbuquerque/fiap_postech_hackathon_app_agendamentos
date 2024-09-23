package com.fiap.hackathon.common.interfaces.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentGateway {
    
    Appointment create(Appointment appointment);

    Appointment getAppointmentById(String id);

    List<Appointment> getAppointmentsByPatient(String patientId);

    List<Appointment> getAppointmentsByDoctor(String doctorId);

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date);
    
    Boolean validateDoctorAvailability(String doctorId);

    Boolean validateScheduleAvailability(Appointment appointment) throws AppointmentConflictException;

}
