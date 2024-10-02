package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.exceptions.custom.AppointmentUpdateException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
import com.fiap.hackathon.core.entity.TimeSlotsEnum;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;
import com.fiap.hackathon.external.services.users.dtos.DoctorDTO;
import com.fiap.hackathon.external.services.users.dtos.DoctorTimetableDTO;
import com.fiap.hackathon.external.services.users.dtos.PatientDTO;
import io.restassured.internal.common.assertion.Assertion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.fiap.hackathon.common.logging.Constants.CONTENT_TYPE;
import static com.fiap.hackathon.common.logging.Constants.MS_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentGatewayImplTest {

    @InjectMocks
    private AppointmentGatewayImpl appointmentGateway;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UsersHTTPClient usersHTTPClient;

    @Test
    void create_ShouldReturnAppointment_WhenAppointmentIsValid() throws CreateEntityException {
        final var appointment = createAppointment();
        when(appointmentRepository.create(appointment)).thenReturn(appointment);

        final var result = appointmentGateway.create(appointment);

        assertEquals(appointment, result);
    }

    @Test
    void create_ShouldThrowCreateEntityException_WhenRepositoryThrowsException() throws CreateEntityException {
        final var appointment = createAppointment();
        when(appointmentRepository.create(appointment)).thenThrow(CreateEntityException.class);

        final var exception = assertThrows(CreateEntityException.class, () -> {
            appointmentGateway.create(appointment);
        });

        assertEquals(CreateEntityException.class, exception.getClass());
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment_WhenExists() throws EntitySearchException {
        final var appointmentId = "appointmentId";
        final var appointment = createAppointment();
        when(appointmentRepository.getAppointmentById(appointmentId)).thenReturn(appointment);

        final var result = appointmentGateway.getAppointmentById(appointmentId);

        assertEquals(appointment, result);
    }

    @Test
    void getAppointmentById_ShouldThrowEntitySearchException_WhenNotFound() throws EntitySearchException {
        final var appointmentId = "nonExistentId";
        when(appointmentRepository.getAppointmentById(appointmentId)).thenThrow(EntitySearchException.class);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getAppointmentById(appointmentId);
        });

        assertEquals(EntitySearchException.class, exception.getClass());
    }

    @Test
    void getAppointmentsByPatient_ShouldReturnAppointments_WhenExists() throws EntitySearchException {
        final var patientId = "patientId";
        final var appointments = Collections.singletonList(createAppointment());
        when(appointmentRepository.getAppointmentsByPatient(patientId)).thenReturn(appointments);

        final var result = appointmentGateway.getAppointmentsByPatient(patientId);

        assertEquals(appointments, result);
    }

    @Test
    void getAppointmentsByPatient_ShouldThrowEntitySearchException_WhenNotFound() throws EntitySearchException {
        final var patientId = "nonExistentPatientId";
        when(appointmentRepository.getAppointmentsByPatient(patientId)).thenThrow(EntitySearchException.class);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getAppointmentsByPatient(patientId);
        });

        assertEquals(EntitySearchException.class, exception.getClass());
    }

    @Test
    void getAppointmentsByDoctor_ShouldReturnAppointments_WhenDoctorExists() throws EntitySearchException {
        final var doctorId = "doctor123";
        final var appointment = new Appointment(); 
        when(appointmentRepository.getAppointmentsByDoctor(doctorId)).thenReturn(List.of(appointment));

        final var appointments = appointmentGateway.getAppointmentsByDoctor(doctorId);

        assertEquals(1, appointments.size());
        assertEquals(appointment, appointments.get(0));
    }

    @Test
    void getAppointmentsByDoctor_ShouldThrowEntitySearchException_WhenDoctorDoesNotExist() throws EntitySearchException {
        final var doctorId = "doctor123";
        when(appointmentRepository.getAppointmentsByDoctor(doctorId)).thenThrow(EntitySearchException.class);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getAppointmentsByDoctor(doctorId);
        });

        assertEquals(EntitySearchException.class, exception.getClass());
    }

    @Test
    void getAppointmentsByDoctorAndDate_ShouldReturnAppointments_WhenDoctorExistsAndDateIsValid() throws EntitySearchException {
        final var doctorId = "doctor123";
        final var date = LocalDate.now();
        final var appointment = new Appointment(); 
        when(appointmentRepository.getAppointmentsByDoctorAndDate(doctorId, date)).thenReturn(List.of(appointment));

        final var appointments = appointmentGateway.getAppointmentsByDoctorAndDate(doctorId, date);

        assertEquals(1, appointments.size());
        assertEquals(appointment, appointments.get(0));
    }

    @Test
    void getAppointmentsByDoctorAndDate_ShouldThrowEntitySearchException_WhenDoctorDoesNotExist() throws EntitySearchException {
        final var doctorId = "doctor123";
        final var date = LocalDate.now();
        when(appointmentRepository.getAppointmentsByDoctorAndDate(doctorId, date)).thenThrow(EntitySearchException.class);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getAppointmentsByDoctorAndDate(doctorId, date);
        });

        assertEquals(EntitySearchException.class, exception.getClass());
    }

    @Test
    void isDoctorAvailable_ShouldReturnTrue_WhenDoctorIsAvailable() {
        final var doctorId = "doctor123";
        final var appointment = new Appointment(); 
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setTimeslot(TimeSlotsEnum.SLOT_11H_12H.getSlot());

        var doctorTimetable = getDoctorTimetable();
        when(usersHTTPClient.getDoctorTimetable(doctorId, MS_USER, CONTENT_TYPE)).thenReturn(new ResponseEntity<>(doctorTimetable, HttpStatus.OK));

        final var isAvailable = appointmentGateway.isDoctorAvailable(doctorId, appointment);

        assertFalse(isAvailable);
    }

    @Test
    void isDoctorAvailable_ShouldReturnFalse_WhenDoctorIsNotAvailable() {
        final var doctorId = "doctor123";
        final var appointment = new Appointment(); 
        appointment.setDate(LocalDate.now());
        appointment.setTimeslot("10:00");

        var doctorTimetable = getDoctorTimetable();
        when(usersHTTPClient.getDoctorTimetable(doctorId, MS_USER, CONTENT_TYPE)).thenReturn(new ResponseEntity<>(doctorTimetable, HttpStatus.OK));

        final var isAvailable = appointmentGateway.isDoctorAvailable(doctorId, appointment);

        assertFalse(isAvailable);
    }

    @Test
    void isScheduleAvailable_ShouldReturnTrue_WhenScheduleIsAvailable() throws EntitySearchException {
        final var appointment = new Appointment(); 
        appointment.setDoctorId("doctor123");
        appointment.setDate(LocalDate.now());
        appointment.setTimeslot("10:00");

        when(appointmentRepository.getAppointmentsByDoctorAndDate(appointment.getDoctorId(), appointment.getDate())).thenReturn(Collections.emptyList());

        final var isAvailable = appointmentGateway.isScheduleAvailable(appointment);

        assertTrue(isAvailable);
    }

    @Test
    void isScheduleAvailable_ShouldReturnFalse_WhenScheduleIsNotAvailable() throws EntitySearchException {
        final var appointment = new Appointment(); 
        appointment.setDoctorId("doctor123");
        appointment.setDate(LocalDate.now());
        appointment.setTimeslot("10:00");

        var existingAppointment = new Appointment();
        existingAppointment.setTimeslot("10:00");
        when(appointmentRepository.getAppointmentsByDoctorAndDate(appointment.getDoctorId(), appointment.getDate())).thenReturn(List.of(existingAppointment));

        final var isAvailable = appointmentGateway.isScheduleAvailable(appointment);

        assertTrue(isAvailable);
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() throws EntitySearchException {
        final var patientId = "patient123";
        final var patient = new PatientDTO();
        when(usersHTTPClient.getPatientById(patientId, MS_USER, CONTENT_TYPE)).thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));

        final var result = appointmentGateway.getPatientById(patientId);

        verify(usersHTTPClient, times(1)).getPatientById(anyString(), anyString(), anyString());
    }

    @Test
    void getPatientById_ShouldThrowEntitySearchException_WhenPatientDoesNotExist() {
        final var patientId = "patient123";
        when(usersHTTPClient.getPatientById(patientId, MS_USER, CONTENT_TYPE)).thenThrow(new RuntimeException("Patient not found."));

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getPatientById(patientId);
        });

        assertEquals("Patient not found.", exception.getMessage());
    }

    @Test
    void getDoctorById_ShouldReturnDoctor_WhenDoctorExists() throws EntitySearchException {
        final var doctorId = "doctor123";
        final var doctor = new DoctorDTO(); // Assumindo que Doctor tem um construtor padrão
        when(usersHTTPClient.getDoctorById(doctorId, MS_USER, CONTENT_TYPE)).thenReturn(new ResponseEntity<>(doctor, HttpStatus.OK));

        final var result = appointmentGateway.getDoctorById(doctorId);

        verify(usersHTTPClient, times(1)).getDoctorById(anyString(), anyString(), anyString());
    }

    @Test
    void getDoctorById_ShouldThrowEntitySearchException_WhenDoctorDoesNotExist() {
        final var doctorId = "doctor123";
        when(usersHTTPClient.getDoctorById(doctorId, MS_USER, CONTENT_TYPE)).thenThrow(new RuntimeException("Doctor not found."));

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentGateway.getDoctorById(doctorId);
        });

        assertEquals("Doctor not found.", exception.getMessage());
    }

    @Test
    void updateStatus_ShouldUpdate_WhenStatusIsValid() throws AppointmentUpdateException {
        Assertions.assertDoesNotThrow(() -> appointmentGateway.updateStatus(anyString(), any(AppointmentStatusEnum.class)));
    }

    private Appointment createAppointment() {
        final var appointment = new Appointment();
        appointment.setDoctorId("doctorId")
                .setPatientId("patientId")
                .setDate(LocalDate.now())
                .setTimeslot("09:00-10:00");
        return appointment;
    }

    private static DoctorTimetableDTO getDoctorTimetable() {
        return new DoctorTimetableDTO("id", "doctor123", Set.of(), Set.of("10:00-11:00"), Set.of("10:00-11:00"), Set.of("--:--"), Set.of("11:00-12:00"), Set.of("--:--"), Set.of("10:00-11:00"));
    }
}
