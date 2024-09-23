package com.fiap.hackathon.common.builders;

import com.fiap.hackathon.common.dto.request.CreateAppointmentRequest;
import com.fiap.hackathon.common.dto.response.GetAppointmentResponse;
import com.fiap.hackathon.core.entity.Appointment;

public class AppointmentBuilder {

    public static Appointment fromRequestToDomain(CreateAppointmentRequest request) {
        return new Appointment()
                .setDoctorId(request.getDoctorId())
                .setPatientId(request.getPatientId())
                .setDate(request.getDate())
                .setTimeslot(request.getTimeslot());
    }

    public static GetAppointmentResponse fromDomainToResponse(Appointment appointment) {
        return new GetAppointmentResponse()
                .setId(appointment.getId())
                .setDoctorId(appointment.getDoctorId())
                .setPatientId(appointment.getPatientId())
                .setDate(appointment.getDate())
                .setTimeslot(appointment.getTimeslot())
                .setCreatedAt(appointment.getCreatedAt());
    }
}
