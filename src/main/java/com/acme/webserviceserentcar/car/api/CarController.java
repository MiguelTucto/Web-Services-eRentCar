package com.acme.webserviceserentcar.car.api;

import com.acme.webserviceserentcar.car.resource.CarResource;
import com.acme.webserviceserentcar.car.resource.CreateCarResource;
import com.acme.webserviceserentcar.car.resource.UpdateCarResource;
import com.acme.webserviceserentcar.car.domain.service.CarService;
import com.acme.webserviceserentcar.car.mapping.CarMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;
    private final CarMapper mapper;

    public CarController(CarService carService, CarMapper mapper) {
        this.carService = carService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get All Cars", description = "Get All Cars", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Cars returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CarResource.class))
                    ))
    })
    @GetMapping
    public Page<CarResource> getAllCars(Pageable pageable) {
        return mapper.modelListToPage(carService.getAll(), pageable);
    }

    @Operation(summary = "Get All Cars by Client id", description = "Get All Cars by Client id", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Cars of Client returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CarResource.class))
                    ))
    })
    @GetMapping("client/{clientId}")
    public Page<CarResource> getAllCarsByClientId(@PathVariable Long clientId, Pageable pageable) {
        return carService.getAllCarsByClientId(clientId, pageable).map(mapper::toResource);
    }

    @Operation(summary = "Get Car By Id", description = "Get Car by Id", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarResource.class)
                    ))
    })
    @GetMapping("{carId}")
    public CarResource getCarById(@PathVariable Long carId) {
        return mapper.toResource(carService.getById(carId));
    }

    @Operation(summary = "Create Car", description = "Create Car", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarResource.class)
                    ))
    })
    @PostMapping("client/{clientId}/car-model/{carModelId}")
    public CarResource createCar(@PathVariable Long clientId,
                                 @PathVariable Long carModelId,
                                 @Valid @RequestBody CreateCarResource request) {
        return mapper.toResource(carService.create(clientId, carModelId, mapper.toModel(request)));
    }

    @Operation(summary = "Update Car", description = "Updating Car", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarResource.class)
                    ))
    })
    @PutMapping("{carId}")
    public CarResource updateCar(@PathVariable Long carId, @Valid @RequestBody UpdateCarResource request) {
        return mapper.toResource(carService.update(carId, mapper.toModel(request)));
    }

    @Operation(summary = "Update Car Rate", description = "Updating Car Rate", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Rate updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarResource.class)
                    ))
    })
    @PutMapping("{carId}/rate/{rate}")
    public CarResource updateCarRate(@PathVariable Long carId, @PathVariable int rate) {
        return mapper.toResource(carService.updateRate(carId, rate));
    }

    @Operation(summary = "Delete Car", description = "Delete Car", tags = {"Cars"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable Long carId) {
        return carService.delete(carId);
    }
}
