package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.builders.DoctorBuilder;
import com.fiap.hackathon.common.builders.PatientBuilder;
import com.fiap.hackathon.common.exceptions.custom.AppointmentUpdateException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
import com.fiap.hackathon.core.entity.Doctor;
import com.fiap.hackathon.core.entity.Patient;
import com.fiap.hackathon.core.usecase.AppointmentUseCaseImpl;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

import static com.fiap.hackathon.common.logging.Constants.CONTENT_TYPE;
import static com.fiap.hackathon.common.logging.Constants.MS_USER;

public class AppointmentGatewayImpl implements AppointmentGateway {

    private final AppointmentRepository repository;
    private final UsersHTTPClient usersHTTPClient;

    public AppointmentGatewayImpl(AppointmentRepository repository, UsersHTTPClient usersHTTPClient) {
        this.repository = repository;
        this.usersHTTPClient = usersHTTPClient;
    }

    private static final Logger logger = LogManager.getLogger(AppointmentUseCaseImpl.class);


    @Override
    public Appointment create(Appointment appointment) throws CreateEntityException {
        return repository.create(appointment);
    }

    @Override
    public Appointment getAppointmentById(String id) throws EntitySearchException {
        return repository.getAppointmentById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) throws EntitySearchException {
        return repository.getAppointmentsByPatient(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) throws EntitySearchException {
        return repository.getAppointmentsByDoctor(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date) throws EntitySearchException {
        return repository.getAppointmentsByDoctorAndDate(doctorId, date);
    }

    @Override
    public Boolean isDoctorAvailable(String doctorId, Appointment appointment) {
        final var dayOfWeek = appointment.getDate().getDayOfWeek();
        final var timeslot = appointment.getTimeslot();

        final var doctorTimetable = usersHTTPClient.getDoctorTimetable(doctorId, MS_USER, CONTENT_TYPE).getBody();

        if (doctorTimetable == null) return false;

        final var workingTimeslots = switch (dayOfWeek) {
            case MONDAY:
                yield doctorTimetable.getMonday();
            case TUESDAY:
                yield doctorTimetable.getTuesday();
            case WEDNESDAY:
                yield doctorTimetable.getWednesday();
            case THURSDAY:
                yield doctorTimetable.getThursday();
            case FRIDAY:
                yield doctorTimetable.getFriday();
            case SATURDAY:
                yield doctorTimetable.getSaturday();
            case SUNDAY:
                yield doctorTimetable.getSunday();
        };

        return workingTimeslots.contains(timeslot);
    }

    @Override
    public Boolean isScheduleAvailable(Appointment appointment) throws EntitySearchException {
        final var doctorId = appointment.getDoctorId();
        final var date = appointment.getDate();
        final var timeslot = appointment.getTimeslot();

        final var doctorTimetable = repository.getAppointmentsByDoctorAndDate(doctorId, date);

        return doctorTimetable.stream()
                .filter(ap ->
                        timeslot.equals(ap.getTimeslot()) &&
                                AppointmentStatusEnum.SCHEDULED.equals(ap.getStatus()))
                .findAny()
                .isEmpty();
    }

    @Override
    public Patient getPatientById(String patientId) throws EntitySearchException {
        try {
            final var response = usersHTTPClient.getPatientById(patientId, MS_USER, CONTENT_TYPE).getBody();
            return PatientBuilder.fromDTOtoDomain(response);
        } catch (Exception ex) {
            logger.error(ex);
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_03_DOCTOR_NOT_FOUND, "Patient not found.");
        }
    }

    @Override
    public Doctor getDoctorById(String doctorId) throws EntitySearchException {
        try {
            final var response = usersHTTPClient.getDoctorById(doctorId, MS_USER, CONTENT_TYPE).getBody();
            return DoctorBuilder.fromDTOtoDomain(response);
        } catch (Exception ex) {
            logger.error(ex);
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_04_PATIENT_NOT_FOUND, "Doctor not found.");
        }
    }

    @Override
    public void updateStatus(String id, AppointmentStatusEnum status) throws AppointmentUpdateException {
        repository.updateStatus(id, status);
    }

}
