package com.fiap.hackathon.external.repository;

import com.fiap.hackathon.common.exceptions.custom.AppointmentUpdateException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        final var item = getItem(appointmentId);

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
        final var item = getItem("appointmentId");

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
        final var item = getItem("appointmentId");

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
        final var item = getItem("appointmentId");

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

    @Test
    void shouldUpdateStatusSuccessfully() throws Exception {
        final var id = "123";
        final var status = AppointmentStatusEnum.SCHEDULED;

        appointmentRepository.updateStatus(id, status);

        verify(dynamoDbClient).updateItem(any(UpdateItemRequest.class));
    }

    @Test
    void shouldThrowAppointmentUpdateExceptionWhenUpdateFails() {
        final var id = "123";
        final var status = AppointmentStatusEnum.SCHEDULED;
        final var request = createUpdateItemRequest(id, status);

        doThrow(RuntimeException.class).when(dynamoDbClient).updateItem(request);

        assertThrows(AppointmentUpdateException.class, () -> appointmentRepository.updateStatus(id, status));
    }

    private UpdateItemRequest createUpdateItemRequest(String id, AppointmentStatusEnum status) {
        final var key = new HashMap<String, AttributeValue>();
        key.put("id", AttributeValue.builder().s(id).build());

        final var updatedValues = new HashMap<String, AttributeValueUpdate>();
        updatedValues.put("appointmentStatus",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(status.name()).build())
                        .action(AttributeAction.PUT)
                        .build());

        return UpdateItemRequest.builder()
                .tableName("AppointmentsTable") // Supondo que seja essa a tabela
                .key(key)
                .attributeUpdates(updatedValues)
                .build();
    }


    private static Appointment getAppointment() {
        return new Appointment()
                .setDoctorId("doctor-id")
                .setPatientId("patient-id")
                .setDate(LocalDate.now())
                .setTimeslot("10:00")
                .setStatus(AppointmentStatusEnum.SCHEDULED)
                .setCreatedAt(LocalDateTime.now());
    }

    private static HashMap<String, AttributeValue> getItem(String appointmentId) {
        final var item = new HashMap<String, AttributeValue>();
        item.put("id", AttributeValue.builder().s(appointmentId).build());
        item.put("doctorId", AttributeValue.builder().s("doctor-id").build());
        item.put("patientId", AttributeValue.builder().s("patient-id").build());
        item.put("scheduledDate", AttributeValue.builder().s(LocalDate.now().toString()).build());
        item.put("timeslot", AttributeValue.builder().s("10:00").build());
        item.put("appointmentStatus", AttributeValue.builder().s("SCHEDULED").build());
        item.put("createdAt", AttributeValue.builder().s(LocalDateTime.now().toString()).build());

        return item;
    }
}
