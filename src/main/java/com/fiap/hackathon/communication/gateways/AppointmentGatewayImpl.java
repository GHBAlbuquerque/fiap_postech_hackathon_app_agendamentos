package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.TimeSlotsEnum;
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
    public Appointment create(Appointment appointment) {
        return repository.create(appointment);
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return repository.getAppointmentById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return repository.getAppointmentsByPatient(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return repository.getAppointmentsByDoctor(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date) {
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
    public Boolean isScheduleAvailable(Appointment appointment) throws AppointmentConflictException {
        final var doctorId = appointment.getDoctorId();
        final var date = appointment.getDate();
        final var timeslot = appointment.getTimeslot();

        final var doctorTimetable = repository.getAppointmentsByDoctorAndDate(doctorId, date);

        return doctorTimetable.stream()
                .filter(ap -> timeslot.equals(ap.getTimeslot()))
                .findAny()
                .isEmpty();
    }

}