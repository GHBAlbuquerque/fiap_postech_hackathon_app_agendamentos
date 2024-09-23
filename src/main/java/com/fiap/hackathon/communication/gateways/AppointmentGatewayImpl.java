package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public class AppointmentGatewayImpl implements AppointmentGateway {

    private final AppointmentRepository appointmentRepository;

    public AppointmentGatewayImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment create(Appointment appointment, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public Appointment getAppointmentById(String id, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date, AppointmentGateway gateway) {
        return null;
    }
}
