package com.fiap.hackathon.common.interfaces.usecase;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentUseCase {

    Appointment create(Appointment appointment, AppointmentGateway gateway) throws CreateEntityException, AppointmentConflictException;

    List<Appointment> getAppointmentsByPatient(String patientId, AppointmentGateway gateway);

    List<Appointment> getAppointmentsByDoctor(String doctorId, @Nullable LocalDate date, AppointmentGateway gateway);

}
