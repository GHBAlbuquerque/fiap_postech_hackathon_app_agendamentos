package com.fiap.hackathon.core.usecase;

import com.fiap.hackathon.common.exceptions.custom.*;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.common.interfaces.usecase.AppointmentUseCase;
import com.fiap.hackathon.common.notifications.DoctorNotification;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.Doctor;
import com.fiap.hackathon.core.entity.Patient;
import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class AppointmentUseCaseImpl implements AppointmentUseCase {

    private Doctor doctor;
    private Patient patient;

    private static final Logger logger = LogManager.getLogger(AppointmentUseCaseImpl.class);

    @Override
    public Appointment create(Appointment appointment,
                              AppointmentGateway gateway,
                              NotificationGateway notificationGateway)
            throws CreateEntityException, AppointmentConflictException {
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

            notifyAppointmentCreation(savedAppointment, notificationGateway);

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

    private void areUsersValid(String doctorId, String patientId, AppointmentGateway gateway) throws EntitySearchException {
        logger.info("Checking patient and doctor existence on database.");

        this.patient = gateway.getPatientById(patientId);
        this.doctor = gateway.getDoctorById(doctorId);

        logger.info("Validated.");
    }

    private void isSchedulable(Appointment appointment, String doctorId, AppointmentGateway gateway)
            throws AppointmentConflictException, EntitySearchException {
        logger.info("Validating availability for requested appointment.");

        if (Boolean.FALSE.equals(gateway.isDoctorAvailable(doctorId, appointment))) {

            throw new AppointmentConflictException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    "Selected doctor does not work on selected date/time. Please either select another doctor or change appointment date/time."
            );
        }

        if (Boolean.FALSE.equals(gateway.isScheduleAvailable(appointment))) {

            throw new AppointmentConflictException(
                    ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT,
                    "Selected Date and Time is already taken. Please select another date/time."
            );
        }

        logger.info("Validated.");
    }

    private void notifyAppointmentCreation(Appointment appointment, NotificationGateway notificationGateway)
            throws NotificationException {
        try {
            new Thread(() -> {
                logger.info("Notifying doctor about new appointment...");

                try {
                    notificationGateway.notify(doctor.getEmail(),
                            DoctorNotification.SUBJECT,
                            DoctorNotification.create(
                                    doctor.getName(),
                                    patient.getName(),
                                    appointment.getDate().toString(),
                                    appointment.getTimeslot())
                    );
                } catch (MessagingException e) {
                    logger.error("Error trying to send e-mail: {}", e.getMessage());
                    throw new RuntimeException(e);
                }

                logger.info("Notification sent.");
            }).start();

        } catch (Exception ex) {
            throw new NotificationException(
                    ExceptionCodes.APPOINTMENT_08_NOTIFICATION_FAILED,
                    ex.getMessage()
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
