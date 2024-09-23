package com.fiap.hackathon.common.interfaces.datasources;

import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {

    Appointment create(Appointment appointment) throws CreateEntityException;

    Appointment getAppointmentById(String id) throws EntitySearchException;

    List<Appointment> getAppointmentsByPatient(String patientId) throws EntitySearchException;

    List<Appointment> getAppointmentsByDoctor(String doctorId) throws EntitySearchException;

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date) throws EntitySearchException;
}
