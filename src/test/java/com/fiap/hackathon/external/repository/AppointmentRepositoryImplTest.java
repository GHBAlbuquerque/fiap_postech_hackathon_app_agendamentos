package com.fiap.hackathon.external.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.external.repository.AppointmentRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AppointmentRepositoryImplTest {

    @InjectMocks
    private AppointmentRepositoryImpl appointmentRepository;

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Test
    void create_ShouldReturnAppointment_WhenCreatedSuccessfully() throws CreateEntityException {
        final var appointment = getAppointment();

        final var itemValues = new HashMap<String, AttributeValue>();
        itemValues.put("id", AttributeValue.builder().s(appointment.getId()).build());

        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(null);

        final var createdAppointment = appointmentRepository.create(appointment);

        assertEquals(appointment.getId(), createdAppointment.getId());
    }

    @Test
    void create_ShouldThrowCreateEntityException_WhenCreationFails() {
        final var appointment = getAppointment();

        doThrow(DynamoDbException.class).when(dynamoDbClient).putItem(any(PutItemRequest.class));

        final var exception = assertThrows(CreateEntityException.class, () -> {
            appointmentRepository.create(appointment);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION, exception.getCode());
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment_WhenExists() throws EntitySearchException {
        final var appointmentId = "appointment-id";
        final var item = new HashMap<String, AttributeValue>();
        item.put("id", AttributeValue.builder().s(appointmentId).build());
        item.put("doctorId", AttributeValue.builder().s("doctor-id").build());
        item.put("patientId", AttributeValue.builder().s("patient-id").build());
        item.put("scheduledDate", AttributeValue.builder().s(LocalDate.now().toString()).build());
        item.put("timeslot", AttributeValue.builder().s("10:00").build());
        item.put("createdAt", AttributeValue.builder().s(LocalDateTime.now().toString()).build());

        final var getItemRequest = GetItemRequest.builder()
                .tableName("Appointment")
                .key(Map.of("id", AttributeValue.builder().s(appointmentId).build()))
                .build();

        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().item(item).build());

        final var retrievedAppointment = appointmentRepository.getAppointmentById(appointmentId);

        assertEquals(appointmentId, retrievedAppointment.getId());
    }

    @Test
    void getAppointmentById_ShouldThrowEntitySearchException_WhenNotFound() {
        final var appointmentId = "appointment-id";

        final var getItemRequest = GetItemRequest.builder()
                .tableName("Appointment")
                .key(Map.of("id", AttributeValue.builder().s(appointmentId).build()))
                .build();

        when(dynamoDbClient.getItem(getItemRequest)).thenReturn(GetItemResponse.builder().build());

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentRepository.getAppointmentById(appointmentId);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, exception.getCode());
    }

    @Test
    void getAppointmentsByPatient_ShouldReturnAppointments_WhenExists() throws EntitySearchException {
        final var patientId = "patient-id";
        final var item = new HashMap<String, AttributeValue>();
        item.put("id", AttributeValue.builder().s("appointment-id").build());
        item.put("doctorId", AttributeValue.builder().s("doctor-id").build());
        item.put("patientId", AttributeValue.builder().s(patientId).build());
        item.put("scheduledDate", AttributeValue.builder().s(LocalDate.now().toString()).build());
        item.put("timeslot", AttributeValue.builder().s("10:00").build());
        item.put("createdAt", AttributeValue.builder().s(LocalDateTime.now().toString()).build());

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(QueryResponse.builder().items(List.of(item)).build());

        final var appointments = appointmentRepository.getAppointmentsByPatient(patientId);

        assertEquals(1, appointments.size());
    }

    @Test
    void getAppointmentsByPatient_ShouldThrowEntitySearchException_WhenQueryFails() {
        final var patientId = "patient-id";

        final var queryRequest = QueryRequest.builder()
                .tableName("Appointment")
                .indexName("AppointmentPatientIdIndex")
                .keyConditionExpression("patientId = :val")
                .expressionAttributeValues(Map.of(":val", AttributeValue.builder().s(patientId).build()))
                .build();

        doThrow(DynamoDbException.class).when(dynamoDbClient).query(queryRequest);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentRepository.getAppointmentsByPatient(patientId);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, exception.getCode());
    }

    @Test
    void getAppointmentsByDoctor_ShouldReturnAppointments_WhenExists() throws EntitySearchException {
        final var doctorId = "doctor-id";
        final var item = new HashMap<String, AttributeValue>();
        item.put("id", AttributeValue.builder().s("appointment-id").build());
        item.put("doctorId", AttributeValue.builder().s(doctorId).build());
        item.put("patientId", AttributeValue.builder().s("patient-id").build());
        item.put("scheduledDate", AttributeValue.builder().s(LocalDate.now().toString()).build());
        item.put("timeslot", AttributeValue.builder().s("10:00").build());
        item.put("createdAt", AttributeValue.builder().s(LocalDateTime.now().toString()).build());

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(QueryResponse.builder().items(List.of(item)).build());

        final var appointments = appointmentRepository.getAppointmentsByDoctor(doctorId);

        assertEquals(1, appointments.size());
    }

    @Test
    void getAppointmentsByDoctor_ShouldThrowEntitySearchException_WhenQueryFails() {
        final var doctorId = "doctor-id";

        final var queryRequest = QueryRequest.builder()
                .tableName("Appointment")
                .indexName("AppointmentDoctorIdIndex")
                .keyConditionExpression("doctorId = :val")
                .expressionAttributeValues(Map.of(":val", AttributeValue.builder().s(doctorId).build()))
                .build();

        doThrow(DynamoDbException.class).when(dynamoDbClient).query(queryRequest);

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentRepository.getAppointmentsByDoctor(doctorId);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, exception.getCode());
    }

    @Test
    void getAppointmentsByDoctorAndDate_ShouldReturnAppointments_WhenExists() throws EntitySearchException {
        final var doctorId = "doctor-id";
        final var date = LocalDate.now();
        final var item = new HashMap<String, AttributeValue>();
        item.put("id", AttributeValue.builder().s("appointment-id").build());
        item.put("doctorId", AttributeValue.builder().s(doctorId).build());
        item.put("patientId", AttributeValue.builder().s("patient-id").build());
        item.put("scheduledDate", AttributeValue.builder().s(date.toString()).build());
        item.put("timeslot", AttributeValue.builder().s("10:00").build());
        item.put("createdAt", AttributeValue.builder().s(LocalDateTime.now().toString()).build());

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(QueryResponse.builder().items(List.of(item)).build());

        final var appointments = appointmentRepository.getAppointmentsByDoctorAndDate(doctorId, date);

        assertEquals(1, appointments.size());
    }

    @Test
    void getAppointmentsByDoctorAndDate_ShouldThrowEntitySearchException_WhenQueryFails() {
        final var doctorId = "doctor-id";
        final var date = LocalDate.now();

        doThrow(DynamoDbException.class).when(dynamoDbClient).query(any(QueryRequest.class));

        final var exception = assertThrows(EntitySearchException.class, () -> {
            appointmentRepository.getAppointmentsByDoctorAndDate(doctorId, date);
        });

        assertEquals(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, exception.getCode());
    }


    private static Appointment getAppointment() {
        return new Appointment()
                .setDoctorId("doctor-id")
                .setPatientId("patient-id")
                .setDate(LocalDate.now())
                .setTimeslot("10:00")
                .setCreatedAt(LocalDateTime.now());
    }
}
