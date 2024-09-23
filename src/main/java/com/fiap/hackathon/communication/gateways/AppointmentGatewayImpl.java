package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;

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
                .filter(ap -> timeslot.equals(ap.getTimeslot()))
                .findAny()
                .isEmpty();
    }

    @Override
    public Boolean doesPatientExist(String patientId) throws EntitySearchException {
        try {
            return usersHTTPClient.getPatientById(patientId, MS_USER, CONTENT_TYPE).getBody().getIsActive();
        } catch (Exception ex) {
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_03_DOCTOR_NOT_FOUND, "Patient not found.");
        }
    }

    @Override
    public Boolean doesDoctorExist(String doctorId) throws EntitySearchException {
        try {
            return usersHTTPClient.getDoctorById(doctorId, MS_USER, CONTENT_TYPE).getBody().getIsActive();
        } catch (Exception ex) {
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_04_PATIENT_NOT_FOUND, "Doctor not found.");
        }
    }

}
