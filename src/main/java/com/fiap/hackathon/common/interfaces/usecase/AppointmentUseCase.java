package com.fiap.hackathon.common.interfaces.usecase;

import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentUseCase {

    public Appointment create(Appointment appointment, AppointmentGateway gateway);

    public List<Appointment> getAppointmentsByPatient(String id, AppointmentGateway gateway);

    public List<Appointment> getAppointmentsByDoctor(String id, @Nullable LocalDate date, AppointmentGateway gateway);
}
