package com.acme.webserviceserentcar.car.api;

import com.acme.webserviceserentcar.car.domain.service.CarModelService;
import com.acme.webserviceserentcar.car.mapping.CarModelMapper;
import com.acme.webserviceserentcar.car.resource.CarModelResource;
import com.acme.webserviceserentcar.car.resource.CreateCarModelResource;
import com.acme.webserviceserentcar.car.resource.UpdateCarModelResource;
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
@RequestMapping("/api/v1/car-models")
public class CarModelController {
    private final CarModelService carModelService;
    private final CarModelMapper mapper;

    public CarModelController(CarModelService carModelService, CarModelMapper mapper) {
        this.carModelService = carModelService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get All Car Models", description = "Get All Car Models", tags = {"CarModels"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Car Models returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CarModelResource.class))
                    ))
    })
    @GetMapping
    public Page<CarModelResource> getAllCarModels(Pageable pageable) {
        return mapper.modelListToPage(carModelService.getAll(), pageable);
    }

    @Operation(summary = "Get Car Model By Id", description = "Get Car Model by Id", tags = {"CarModels"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Model returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarModelResource.class)
                    ))
    })
    @GetMapping("{carModelId}")
    public CarModelResource getCarModelById(@PathVariable Long carModelId) {
        return mapper.toResource(carModelService.getById(carModelId));
    }

    @Operation(summary = "Create Car Model", description = "Create Car Model", tags = {"CarModels"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Model created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarModelResource.class)
                    ))
    })
    @PostMapping("/car-brands/{carBrandId}")
    public CarModelResource createCarModel(@PathVariable Long carBrandId, @Valid @RequestBody CreateCarModelResource request) {
        return mapper.toResource(carModelService.create(carBrandId, mapper.toModel(request)));
    }

    @Operation(summary = "Update Car Model", description = "Updating Car Model", tags = {"CarModels"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Model updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarModelResource.class)
                    ))
    })
    @PutMapping("{carModelId}")
    public CarModelResource updateCarModel(@PathVariable Long carModelId, @Valid @RequestBody UpdateCarModelResource request) {
        return mapper.toResource(carModelService.update(carModelId, mapper.toModel(request)));
    }

    @Operation(summary = "Delete Car Model", description = "Delete Car Model", tags = {"CarModels"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Model deleted",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{carModelId}")
    public ResponseEntity<?> deleteCarModel(@PathVariable Long carModelId) {
        return carModelService.delete(carModelId);
    }
}
