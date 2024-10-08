package com.fiap.hackathon.communication.controllers;

import com.fiap.hackathon.common.builders.AppointmentBuilder;
import com.fiap.hackathon.common.dto.request.CreateAppointmentRequest;
import com.fiap.hackathon.common.dto.response.GetAppointmentResponse;
import com.fiap.hackathon.common.exceptions.custom.*;
import com.fiap.hackathon.common.exceptions.model.ExceptionDetails;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.gateways.AuthenticationGateway;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.common.interfaces.usecase.AppointmentUseCase;
import com.fiap.hackathon.core.entity.Appointment;
import com.fiap.hackathon.core.entity.AppointmentStatusEnum;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.fiap.hackathon.common.exceptions.custom.ExceptionCodes.APPOINTMENT_06_INVALID_SEARCH;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentGateway gateway;
    private final AppointmentUseCase useCase;
    private final NotificationGateway notificationGateway;
    private final AuthenticationGateway authenticationGateway;

    public AppointmentController(AppointmentGateway gateway, AppointmentUseCase useCase, NotificationGateway notificationGateway, AuthenticationGateway authenticationGateway) {
        this.gateway = gateway;
        this.useCase = useCase;
        this.notificationGateway = notificationGateway;
        this.authenticationGateway = authenticationGateway;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class)))
    })
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GetAppointmentResponse> createAppointment(
            @RequestBody @Valid CreateAppointmentRequest request,
            @RequestHeader String user_email
    ) throws AppointmentConflictException, CreateEntityException, AuthenticationException {
        authenticationGateway.validatePatientCaller(user_email, request.getPatientId());

        final var appointment = AppointmentBuilder.fromRequestToDomain(request);
        final var result = useCase.create(appointment, gateway, notificationGateway);
        final var id = result.getId();

        final var uri = URI.create(id);

        return ResponseEntity
                .created(uri)
                .body(
                        AppointmentBuilder.fromDomainToResponse(result)
                );
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class)))
    })
    @GetMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<GetAppointmentResponse>> getAppointments(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) LocalDate date
    ) throws EntitySearchException {
        List<Appointment> result = List.of();

        if (patientId == null && doctorId == null)
            throw new EntitySearchException(
                    APPOINTMENT_06_INVALID_SEARCH,
                    "Please inform either patientId or doctorId for search."
            );

        if (patientId != null)
            result = useCase.getAppointmentsByPatient(patientId, gateway);

        if (doctorId != null)
            result = useCase.getAppointmentsByDoctor(doctorId, date, gateway);


        return ResponseEntity.ok(
                result.stream()
                        .map(
                                AppointmentBuilder::fromDomainToResponse
                        )
                        .toList()
        );
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class)))
    })
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateStatus(@PathVariable String id,
                                          @RequestParam AppointmentStatusEnum status,
                                          @RequestHeader String user_email)
            throws AppointmentUpdateException {
        useCase.updateStatus(id, status, gateway);
        return ResponseEntity.noContent().build();
    }

}
