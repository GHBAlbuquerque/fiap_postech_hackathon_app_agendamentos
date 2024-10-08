package com.fiap.hackathon.common.logging;

public class LoggingPattern {
    public static final String CREATE_ENTITY_SUCCESS = "{} was successfully created. The entity id is {}.";
    public static final String CREATE_ENTITY_ERROR = "Error creating entity on database: {}";

    public static final String GET_ENTITY_SUCCESS = "Entity with attribute '{}' found on table '{}'";
    public static final String GET_ENTITY_ERROR = "Error looking for entity with attribute {} on database: {}";

    public static final String UPDATE_ENTITY_SUCCESS = "Entity with id '{}'updated on table '{}'";
    public static final String UPDATE_ENTITY_ERROR = "Error updating entity with id {} on database: {}";
}
