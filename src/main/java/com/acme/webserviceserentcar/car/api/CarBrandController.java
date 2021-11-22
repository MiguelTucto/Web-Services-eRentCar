package com.acme.webserviceserentcar.car.api;

import com.acme.webserviceserentcar.car.domain.service.CarBrandService;
import com.acme.webserviceserentcar.car.mapping.CarBrandMapper;
import com.acme.webserviceserentcar.car.resource.*;
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
@RequestMapping("/api/v1/car-brands")
public class CarBrandController {
    private final CarBrandService carBrandService;
    private final CarBrandMapper mapper;

    public CarBrandController(CarBrandService carBrandService, CarBrandMapper mapper) {
        this.carBrandService = carBrandService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get All Car Brands", description = "Get All Car Brands", tags = {"CarBrands"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Car Brands returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CarBrandResource.class))
                    ))
    })
    @GetMapping
    public Page<CarBrandResource> getAllCarBrands(Pageable pageable) {
        return mapper.modelListToPage(carBrandService.getAll(), pageable);
    }

    @Operation(summary = "Get Car Brand By Id", description = "Get Car Brand by Id", tags = {"CarBrands"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Brand returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarBrandResource.class)
                    ))
    })
    @GetMapping("{carBrandId}")
    public CarBrandResource getCarBrandById(@PathVariable Long carBrandId) {
        return mapper.toResource(carBrandService.getById(carBrandId));
    }

    @Operation(summary = "Create Car Brand", description = "Create Car Brand", tags = {"CarBrands"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Brand created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarBrandResource.class)
                    ))
    })
    @PostMapping()
    public CarBrandResource createCarBrand(@Valid @RequestBody CreateCarBrandResource request) {
        return mapper.toResource(carBrandService.create(mapper.toModel(request)));
    }

    @Operation(summary = "Update Car Brand", description = "Updating Car Brand", tags = {"CarBrands"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Brand updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarBrandResource.class)
                    ))
    })
    @PutMapping("{carBrandId}")
    public CarBrandResource updateCarBrand(@PathVariable Long carBrandId, @Valid @RequestBody UpdateCarBrandResource request) {
        return mapper.toResource(carBrandService.update(carBrandId, mapper.toModel(request)));
    }

    @Operation(summary = "Delete Car Brand", description = "Delete Car Brand", tags = {"CarBrands"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car Brand deleted",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{carBrandId}")
    public ResponseEntity<?> deleteCarBrand(@PathVariable Long carBrandId) {
        return carBrandService.delete(carBrandId);
    }
}
