package com.fiap.hackathon.external.repository;

import com.fiap.hackathon.common.exceptions.custom.AppointmentUpdateException;
import com.fiap.hackathon.common.exceptions.custom.CreateEntityException;
import com.fiap.hackathon.common.exceptions.custom.EntitySearchException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.fiap.hackathon.common.exceptions.custom.ExceptionCodes.APPOINTMENT_01_NOT_FOUND;
import static com.fiap.hackathon.common.logging.LoggingPattern.*;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private static final String TABLE_NAME = "Appointment";
    private static final String PATIENT_ID_INDEX = "AppointmentPatientIdIndex";
    private static final String DOCTOR_ID_INDEX = "AppointmentDoctorIdIndex";
    private static final String DOCTOR_ID_DATE_INDEX = "AppointmentDoctorIdDateIndex";
    private static final String ATTRIBUTES = "id, patientId, doctorId, scheduledDate, timeslot, appointmentStatus, createdAt";

    private final DynamoDbClient dynamoDbClient;

    public AppointmentRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private static final Logger logger = LogManager.getLogger(AppointmentRepositoryImpl.class);

    @Override
    public Appointment create(Appointment appointment) throws CreateEntityException {
        final var itemValues = convertEntityToItem(appointment);

        final var putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(itemValues)
                .build();

        try {
            dynamoDbClient.putItem(putItemRequest);
            final var id = itemValues.get("id").s();

            logger.info(CREATE_ENTITY_SUCCESS, TABLE_NAME, id);

            appointment.setId(id);
            return appointment;

        } catch (DynamoDbException e) {
            logger.error(CREATE_ENTITY_ERROR, e.getMessage());
            throw new CreateEntityException(ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION, e.getMessage());
        }
    }

    @Override
    public Appointment getAppointmentById(String id) throws EntitySearchException {
        final var key = new HashMap<String, AttributeValue>();
        key.put("id", AttributeValue.builder().s(id).build());

        final var getItemRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        try {
            final var result = dynamoDbClient.getItem(getItemRequest);

            if (result.item().isEmpty()) {
                throw new EntitySearchException(APPOINTMENT_01_NOT_FOUND, "No appointment was found with the requested id.");
            }

            logger.info(GET_ENTITY_SUCCESS, id, TABLE_NAME);

            return convertItemToEntity(result.item());

        } catch (Exception e) {
            logger.error(GET_ENTITY_ERROR, id, e.getMessage());
            throw new EntitySearchException(APPOINTMENT_01_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) throws EntitySearchException {
        final var expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", AttributeValue.builder().s(patientId).build());

        try {
            final var queryRequest = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .indexName(PATIENT_ID_INDEX)
                    .keyConditionExpression("patientId = :val")
                    .projectionExpression(ATTRIBUTES)
                    .expressionAttributeValues(expressionAttributeValues)
                    .build();

            final var result = dynamoDbClient.query(queryRequest);

            if (result.items().isEmpty()) return Collections.emptyList();

            return result.items().stream().map(this::convertItemToEntity).toList();

        } catch (Exception e) {
            logger.error(GET_ENTITY_ERROR, patientId, e.getMessage());
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) throws EntitySearchException {
        final var expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", AttributeValue.builder().s(doctorId).build());

        try {
            final var queryRequest = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .indexName(DOCTOR_ID_INDEX)
                    .keyConditionExpression("doctorId = :val")
                    .projectionExpression(ATTRIBUTES)
                    .expressionAttributeValues(expressionAttributeValues)
                    .build();

            final var result = dynamoDbClient.query(queryRequest);

            if (result.items().isEmpty()) return Collections.emptyList();

            return result.items().stream().map(this::convertItemToEntity).toList();

        } catch (Exception e) {
            logger.error(GET_ENTITY_ERROR, doctorId, e.getMessage());
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date) throws EntitySearchException {
        final var expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", AttributeValue.builder().s(doctorId).build());
        expressionAttributeValues.put(":scheduledDate", AttributeValue.builder().s(date.toString()).build());

        try {
            final var queryRequest = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .indexName(DOCTOR_ID_DATE_INDEX)
                    .keyConditionExpression("doctorId = :val and scheduledDate = :scheduledDate")
                    .projectionExpression(ATTRIBUTES)
                    .expressionAttributeValues(expressionAttributeValues)
                    //.consistentRead(true)
                    .build();

            final var result = dynamoDbClient.query(queryRequest);

            if (result.items().isEmpty()) return Collections.emptyList();

            return result.items().stream().map(this::convertItemToEntity).toList();

        } catch (Exception e) {
            logger.error(GET_ENTITY_ERROR, doctorId, e.getMessage());
            throw new EntitySearchException(ExceptionCodes.APPOINTMENT_01_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public void updateStatus(String id, AppointmentStatusEnum status) throws AppointmentUpdateException {
        final var key = new HashMap<String, AttributeValue>();
        key.put("id", AttributeValue.builder().s(id).build());

        final var updatedValues = new HashMap<String, AttributeValueUpdate>();
        updatedValues.put("appointmentStatus",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(status.name()).build())
                        .action(AttributeAction.PUT)
                        .build());

        final var request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .attributeUpdates(updatedValues)
                .build();

        try {
            dynamoDbClient.updateItem(request);
            logger.info(UPDATE_ENTITY_SUCCESS, id, TABLE_NAME);

        } catch (Exception e) {
            logger.error(UPDATE_ENTITY_ERROR, id, e.getMessage());
            throw new AppointmentUpdateException(ExceptionCodes.APPOINTMENT_09_APPOINTMENT_UPDATE, e.getMessage());
        }
    }

    private HashMap<String, AttributeValue> convertEntityToItem(Appointment appointment) {
        final var itemValues = new HashMap<String, AttributeValue>();

        if (appointment.getId() == null)
            itemValues.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());

        if (appointment.getId() != null)
            itemValues.put("id", AttributeValue.builder().s(appointment.getId()).build());

        itemValues.put("doctorId", AttributeValue.builder().s(appointment.getDoctorId()).build());
        itemValues.put("patientId", AttributeValue.builder().s(appointment.getPatientId()).build());
        itemValues.put("scheduledDate", AttributeValue.builder().s(appointment.getDate().toString()).build());
        itemValues.put("timeslot", AttributeValue.builder().s(appointment.getTimeslot()).build());
        itemValues.put("appointmentStatus", AttributeValue.builder().s(appointment.getStatus().name()).build());
        itemValues.put("createdAt", AttributeValue.builder().s(appointment.getCreatedAt().toString()).build());

        return itemValues;
    }

    private Appointment convertItemToEntity(Map<String, AttributeValue> item) {
        return new Appointment()
                .setId(item.get("id").s())
                .setDoctorId(item.get("doctorId").s())
                .setPatientId(item.get("patientId").s())
                .setDate(LocalDate.parse(item.get("scheduledDate").s()))
                .setTimeslot(item.get("timeslot").s())
                .setStatus(AppointmentStatusEnum.valueOf(item.get("appointmentStatus").s()))
                .setCreatedAt(LocalDateTime.parse(item.get("createdAt").s()));
    }
}
