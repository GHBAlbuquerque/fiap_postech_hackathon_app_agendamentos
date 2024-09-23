package com.fiap.hackathon.core.usecase;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.usecase.AppointmentUseCase;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class AppointmentUseCaseImpl implements AppointmentUseCase {

    private static final Logger logger = LogManager.getLogger(AppointmentUseCaseImpl.class);

    @Override
    public Appointment create(Appointment appointment, AppointmentGateway gateway) throws CreateEntityException, AppointmentConflictException {
        final var doctorId = appointment.getDoctorId();
        final var patientId = appointment.getPatientId();

        logger.info("Creating APPOINTMENT for patient [patientId '{}'] with doctor [doctorId '{}']",
                patientId,
                doctorId
        );

        try {
            logger.info("Validating request.");

            appointment.isValid();
            areUsersValid(doctorId, patientId, gateway);
            isSchedulable(appointment, doctorId, gateway);

            final var savedAppointment = gateway.create(appointment);

            logger.info("APPOINTMENT successfully created.");

            return savedAppointment;

        } catch (AppointmentConflictException ex) {
            logger.error("APPOINTMENT creation failed. Error: {}", ex.getMessage());

            throw new AppointmentConflictException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    ex.getMessage()
            );

        } catch (Exception ex) {
            logger.error("APPOINTMENT creation failed. Error: {}", ex.getMessage());

            throw new CreateEntityException(
                    ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION,
                    ex.getMessage()
            );
        }
    }

    private static void areUsersValid(String doctorId, String patientId, AppointmentGateway gateway) throws EntitySearchException {
        logger.info("Checking patient and doctor existence on database.");

        gateway.doesDoctorExist(doctorId);
        gateway.doesPatientExist(patientId);
    }

    private static void isSchedulable(Appointment appointment, String doctorId, AppointmentGateway gateway)
            throws AppointmentConflictException, EntitySearchException {
        logger.info("Validating availability for requested appointment.");

        if (Boolean.FALSE.equals(gateway.isDoctorAvailable(doctorId, appointment)) ||
                Boolean.FALSE.equals(gateway.isScheduleAvailable(appointment))) {

            throw new AppointmentConflictException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    "Selected Date and Time is already taken. Please select another date/time."
            );
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId, AppointmentGateway gateway) throws EntitySearchException {
        logger.info("Getting APPOINTMENTS for patient [patientId '{}'].", patientId);

        return gateway.getAppointmentsByPatient(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId, @Nullable LocalDate date, AppointmentGateway gateway) throws EntitySearchException {
        logger.info("Getting APPOINTMENTS for doctor [doctorId '{}'].", doctorId);

        if (date != null)
            return gateway.getAppointmentsByDoctorAndDate(doctorId, date);

        return gateway.getAppointmentsByDoctor(doctorId);
    }


}
