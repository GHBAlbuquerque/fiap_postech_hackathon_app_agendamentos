package com.fiap.hackathon.core.usecase;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
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
            gateway.validateDoctorAvailability(doctorId);
            gateway.validateScheduleAvailability(appointment);

            final var savedAppointment = gateway.create(appointment);

            logger.info("APPOINTMENT successfully created.");

            return savedAppointment;

        } catch (AppointmentConflictException ex) {
            logger.error("APPOINTMENT creation failed.");

            throw new AppointmentConflictException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    ex.getMessage()
            );

        } catch (Exception ex) {
            logger.error("APPOINTMENT creation failed.");

            throw new CreateEntityException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    ex.getMessage()
            );
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId, AppointmentGateway gateway) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId, @Nullable LocalDate date, AppointmentGateway gateway) {
        return null;
    }


}
