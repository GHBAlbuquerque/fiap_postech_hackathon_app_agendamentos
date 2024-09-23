package com.fiap.hackathon.external.repository;


import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.core.entity.Appointment;
import jakarta.annotation.Nullable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.LocalDate;
import java.util.List;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private static final String TABLE_NAME = "Appointment";
    private static final String PATIENT_ID_INDEX = "AppointmentPatientIdIndex";
    private static final String DOCTOR_ID_INDEX = "AppointmentDoctorIdIndex";
    private static final String DOCTOR_ID_DATE_INDEX = "AppointmentDoctorIdDateIndex";
    private static final String ATTRIBUTES = "id, patientId, doctorId, date, timeslot, createdAt";

    private final DynamoDbClient dynamoDbClient;

    public AppointmentRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public Appointment create(Appointment appointment) {
        return null;
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, @Nullable LocalDate date) {
        return null;
    }

    /*
    private static final String TABLE_NAME = "Timetable";
    private static final String DOCTOR_ID_INDEX = "DoctorIdIndex";
    private static final String ATTRIBUTES = "id,doctorId,sunday,monday,tuesday,wednesday,thursday,friday,saturday";

    private final DynamoDbClient dynamoDbClient;

    public AppointmentRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private static final Logger logger = LogManager.getLogger(TimetableRepositoryImpl.class);

    @Override
    public DoctorTimetable save(DoctorTimetable doctorTimetable) throws CreateEntityException {
        final var itemValues = convertEntityToItem(doctorTimetable);

        final var putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(itemValues)
                .build();

        try {
            dynamoDbClient.putItem(putItemRequest);
            final var id = itemValues.get("id").s();

            logger.info(CREATE_ENTITY_SUCCESS, TABLE_NAME, id);

            doctorTimetable.setId(id);
            return doctorTimetable;

        } catch (DynamoDbException e) {
            logger.error(CREATE_ENTITY_ERROR, e.getMessage());
            throw new CreateEntityException(USER_08_USER_CREATION, e.getMessage());
        }
    }

    @Override
    public DoctorTimetable getTimetableByDoctorId(String doctorId) throws EntitySearchException {
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

            if (result.items().isEmpty()) return null;

            final var timetables = result.items().stream().map(this::convertItemToEntity).toList();

            return timetables.get(0);

        } catch (Exception e) {
            logger.error(GET_ENTITY_ERROR, doctorId, e.getMessage());
            throw new EntitySearchException(USER_01_NOT_FOUND, e.getMessage());
        }
    }

    private HashMap<String, AttributeValue> convertEntityToItem(DoctorTimetable doctorTimetable) {
        final var itemValues = new HashMap<String, AttributeValue>();

        if (doctorTimetable.getId() == null)
            itemValues.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());

        if (doctorTimetable.getId() != null)
            itemValues.put("id", AttributeValue.builder().s(doctorTimetable.getId()).build());

        itemValues.put("doctorId", AttributeValue.builder().s(doctorTimetable.getDoctorId()).build());
        itemValues.put("sunday", AttributeValue.builder().ss(doctorTimetable.getSunday()).build());
        itemValues.put("monday", AttributeValue.builder().ss(doctorTimetable.getMonday()).build());
        itemValues.put("tuesday", AttributeValue.builder().ss(doctorTimetable.getTuesday()).build());
        itemValues.put("wednesday", AttributeValue.builder().ss(doctorTimetable.getWednesday()).build());
        itemValues.put("thursday", AttributeValue.builder().ss(doctorTimetable.getThursday()).build());
        itemValues.put("friday", AttributeValue.builder().ss(doctorTimetable.getFriday()).build());
        itemValues.put("saturday", AttributeValue.builder().ss(doctorTimetable.getSaturday()).build());

        return itemValues;
    }

    private DoctorTimetable convertItemToEntity(Map<String, AttributeValue> item) {
        return new DoctorTimetable(
                item.get("id").s(),
                item.get("doctorId").s(),
                new HashSet<>(item.get("sunday").ss()),
                new HashSet<>(item.get("monday").ss()),
                new HashSet<>(item.get("tuesday").ss()),
                new HashSet<>(item.get("wednesday").ss()),
                new HashSet<>(item.get("thursday").ss()),
                new HashSet<>(item.get("friday").ss()),
                new HashSet<>(item.get("saturday").ss())
        );
    }*/
}
