package com.fiap.hackathon.core.usecase;

import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.usecase.AppointmentUseCase;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public class AppointmentUseCaseImpl implements AppointmentUseCase {
    //TODO

    @Override
    public Appointment create(Appointment appointment, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String id, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String id, @Nullable LocalDate date, AppointmentGateway gateway) {
        return null;
    }


}
