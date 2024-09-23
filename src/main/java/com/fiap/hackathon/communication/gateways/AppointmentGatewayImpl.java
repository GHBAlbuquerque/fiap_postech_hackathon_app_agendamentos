package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;

public class AppointmentGatewayImpl implements AppointmentGateway {

    private final AppointmentRepository appointmentRepository;

    public AppointmentGatewayImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
}
