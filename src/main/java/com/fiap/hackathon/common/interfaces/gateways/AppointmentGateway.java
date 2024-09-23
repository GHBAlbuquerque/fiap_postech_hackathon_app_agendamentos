package com.fiap.hackathon.common.interfaces.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.core.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentGateway {
    
    Appointment create(Appointment appointment) throws CreateEntityException;

    Appointment getAppointmentById(String id) throws EntitySearchException;

    List<Appointment> getAppointmentsByPatient(String patientId) throws EntitySearchException;

    List<Appointment> getAppointmentsByDoctor(String doctorId) throws EntitySearchException;

    List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date) throws EntitySearchException;
    
    Boolean isDoctorAvailable(String doctorId, Appointment appointment);

    Boolean isScheduleAvailable(Appointment appointment) throws AppointmentConflictException, EntitySearchException;

    Boolean doesPatientExist(String patientId) throws EntitySearchException;

    Boolean doesDoctorExist(String doctorId) throws EntitySearchException;

}
