package com.fiap.hackathon.common.interfaces.datasources;

import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {

    Appointment create(Appointment appointment, AppointmentGateway gateway);

    Appointment getAppointmentById(String id, AppointmentGateway gateway);

    List<Appointment> getAppointmentsByPatient(String patientId, AppointmentGateway gateway);

    List<Appointment> getAppointmentsByDoctor(String doctorId, AppointmentGateway gateway);

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date, AppointmentGateway gateway);
}
