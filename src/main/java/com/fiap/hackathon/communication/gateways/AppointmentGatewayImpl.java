package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public class AppointmentGatewayImpl implements AppointmentGateway {

    private final AppointmentRepository appointmentRepository;
    private final UsersHTTPClient usersHTTPClient;

    public AppointmentGatewayImpl(AppointmentRepository appointmentRepository, UsersHTTPClient usersHTTPClient) {
        this.appointmentRepository = appointmentRepository;
        this.usersHTTPClient = usersHTTPClient;
    }


    @Override
    public Appointment create(Appointment appointment) {
        return null;
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date) {
        return null;
    }

    @Override
    public Boolean validateDoctorAvailability(String doctorId) {
        return null;
    }

    @Override
    public Boolean validateScheduleAvailability(Appointment appointment) throws AppointmentConflictException {
        return null;
    }
}
