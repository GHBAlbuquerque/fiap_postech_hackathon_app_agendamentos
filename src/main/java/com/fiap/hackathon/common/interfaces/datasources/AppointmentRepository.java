package com.fiap.hackathon.common.interfaces.datasources;

import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {

    Appointment create(Appointment appointment);

    Appointment getAppointmentById(String id);

    List<Appointment> getAppointmentsByPatient(String patientId);

    List<Appointment> getAppointmentsByDoctor(String doctorId);

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date);
}
