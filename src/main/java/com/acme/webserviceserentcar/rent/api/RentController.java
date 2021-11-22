package com.acme.webserviceserentcar.rent.api;

import com.acme.webserviceserentcar.rent.domain.service.RentService;
import com.acme.webserviceserentcar.rent.mapping.RentMapper;
import com.acme.webserviceserentcar.rent.resource.CreateRentResource;
import com.acme.webserviceserentcar.rent.resource.RentResource;
import com.acme.webserviceserentcar.rent.resource.UpdateRentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/rents")
public class RentController {
    private final RentService rentService;
    private final RentMapper mapper;

    public RentController(RentService rentService, RentMapper mapper) {
        this.rentService = rentService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get All Rents", description = "Get All Rents on pages", tags = {"Rents"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Rents returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentResource.class))
                    ))
    })
    @GetMapping
    public Page<RentResource> getAllRents(Pageable pageable) {
        return mapper.modelListToPage(rentService.getAll(), pageable);
    }


    @Operation(summary = "Get Rent by Id", description = "Get Rent by Id", tags = {"Rents"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rent returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentResource.class)
                    ))
    })
    @GetMapping("{rentId}")
    public RentResource getRentById(@PathVariable Long rentId) {
        return mapper.toResource(rentService.getById(rentId));
    }


    @Operation(summary = "Create Rent", description = "Create Rent", tags = {"Rents"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rent created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentResource.class)
                    ))
    })
    @PostMapping("client/{clientId}/car/{carId}")
    public RentResource createRent(@PathVariable Long clientId,
                                   @PathVariable Long carId,
                                   @Valid @RequestBody CreateRentResource request) {
        return mapper.toResource(rentService.create(clientId, carId, mapper.toModel(request)));
    }


    @Operation(summary = "Update Rent", description = "Update Rent", tags = {"Rents"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rent updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentResource.class)
                    ))
    })
    @PutMapping("{rentId}")
    public RentResource updateRent(@PathVariable Long rentId, @Valid @RequestBody UpdateRentResource request) {
        return mapper.toResource(rentService.update(rentId, mapper.toModel(request)));
    }


    @Operation(summary = "Delete Rent", description = "Delete Rent", tags = {"Rents"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rent deleted", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{rentId}")
    public ResponseEntity<?> deleteRent(@PathVariable Long rentId) {
        return rentService.delete(rentId);
    }

}
