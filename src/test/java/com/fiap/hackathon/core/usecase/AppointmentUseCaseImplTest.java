package com.fiap.hackathon.core.usecase;

import com.fiap.hackathon.common.exceptions.custom.AppointmentConflictException;
import com.fiap.hackathon.common.exceptions.custom.AppointmentUpdateException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
import com.fiap.hackathon.core.entity.Doctor;
import com.fiap.hackathon.core.entity.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentUseCaseImplTest {

    @InjectMocks
    private AppointmentUseCaseImpl appointmentUseCase;

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @Test
    void givenValidAppointment_whenCreate_thenReturnSavedAppointment() throws Exception {
        final var appointment = createAppointment();
        final var expectedAppointment = new Appointment(/* Dados esperados */);

        when(appointmentGateway.create(appointment)).thenReturn(expectedAppointment);
        when(appointmentGateway.getPatientById(appointment.getPatientId())).thenReturn(new Patient());
        when(appointmentGateway.getDoctorById(appointment.getDoctorId())).thenReturn(new Doctor());
        when(appointmentGateway.isDoctorAvailable(appointment.getDoctorId(), appointment)).thenReturn(true);
        when(appointmentGateway.isScheduleAvailable(appointment)).thenReturn(true);

        final var savedAppointment = appointmentUseCase.create(appointment, appointmentGateway, notificationGateway);

        assertEquals(expectedAppointment, savedAppointment);
    }

   /*@Test
    void createAppointment_ShouldThrowCreateEntityException_WhenUnexpectedErrorOccurs() throws CreateEntityException {
        final var appointment = createAppointment();
        doThrow(new RuntimeException("Unexpected error")).when(appointmentGateway).create(any(Appointment.class));

        final var exception = assertThrows(CreateEntityException.class, () -> {
            appointmentUseCase.create(appointment, appointmentGateway, notificationGateway);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION, exception.getCode());
    }*/

    @Test
    void givenAppointmentConflict_whenCreate_thenThrowAppointmentConflictException() throws EntitySearchException {
        final var appointment = createAppointment();

        when(appointmentGateway.getPatientById(appointment.getPatientId())).thenReturn(new Patient());
        when(appointmentGateway.getDoctorById(appointment.getDoctorId())).thenReturn(new Doctor());
        when(appointmentGateway.isDoctorAvailable(appointment.getDoctorId(), appointment)).thenReturn(false);

        var exception = assertThrows(AppointmentConflictException.class, () ->
                appointmentUseCase.create(appointment, appointmentGateway, notificationGateway));

        assertEquals(ExceptionCodes.APPOINTMENT_05_APPOINTMENT_CONFLICT, exception.getCode());
    }

    @Test
    void givenInvalidPatientId_whenGetAppointmentsByPatient_thenThrowEntitySearchException() throws EntitySearchException {
        final var patientId = "invalid-patient-id";

        when(appointmentGateway.getAppointmentsByPatient(patientId)).thenThrow(EntitySearchException.class);

        var exception = assertThrows(EntitySearchException.class, () ->
                appointmentUseCase.getAppointmentsByPatient(patientId, appointmentGateway));

        assertEquals(EntitySearchException.class, exception.getClass());
    }

    @Test
    void givenDoctorId_whenGetAppointmentsByDoctor_thenReturnAppointments() throws Exception {
        final var doctorId = "some-doctor-id";
        final var appointments = Collections.singletonList(new Appointment());

        when(appointmentGateway.getAppointmentsByDoctor(doctorId)).thenReturn(appointments);

        final var result = appointmentUseCase.getAppointmentsByDoctor(doctorId, null, appointmentGateway);

        assertEquals(appointments, result);
    }

    @Test
    void shouldUpdateStatusSuccessfully() throws Exception {
        final var appointment = createAppointment();
        final var id = "123";
        final var status = AppointmentStatusEnum.CANCELED;

        when(appointmentGateway.getAppointmentById(id)).thenReturn(appointment);
        doNothing().when(appointmentGateway).updateStatus(id, status);

        appointmentUseCase.updateStatus(id, status, appointmentGateway);

        verify(appointmentGateway).updateStatus(id, status);
    }

    @Test
    void shouldThrowAppointmentUpdateExceptionWhenStatusChangeIsInvalid() throws Exception {
        final var appointment = createAppointment();
        final var id = "123";
        final var status = AppointmentStatusEnum.SCHEDULED;

        when(appointmentGateway.getAppointmentById(id)).thenReturn(appointment);

        assertThrows(AppointmentUpdateException.class, () -> appointmentUseCase.updateStatus(id, status, appointmentGateway));
    }

    @Test
    void shouldThrowAppointmentUpdateExceptionWhenGatewayFailsToUpdateStatus() throws Exception {
        final var appointment = createAppointment();
        final var id = "123";
        final var status = AppointmentStatusEnum.COMPLETED;

        when(appointmentGateway.getAppointmentById(id)).thenReturn(appointment);
        doThrow(RuntimeException.class).when(appointmentGateway).updateStatus(id, status);

        assertThrows(AppointmentUpdateException.class, () -> appointmentUseCase.updateStatus(id, status, appointmentGateway));
    }


    private Appointment createAppointment() {
        final var currentDateTime = LocalDateTime.now();
        return new Appointment()
                .setId("1")
                .setDoctorId("da347bea-f772-4db4-864d-5e4a622dcc08")
                .setPatientId("28831743-c53d-451a-89ef-bc464176f2ed")
                .setDate(LocalDate.now().plusDays(1))
                .setTimeslot("14:00-15:00")
                .setStatus(AppointmentStatusEnum.SCHEDULED)
                .setCreatedAt(currentDateTime);
    }

}
