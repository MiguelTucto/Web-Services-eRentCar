package com.acme.webserviceserentcar.favourite.api;

import com.acme.webserviceserentcar.car.resource.CarResource;
import com.acme.webserviceserentcar.client.domain.service.ClientService;
import com.acme.webserviceserentcar.client.resource.ClientResource;
import com.acme.webserviceserentcar.client.resource.PlanResource;
import com.acme.webserviceserentcar.favourite.domain.service.FavouriteService;
import com.acme.webserviceserentcar.favourite.mapping.FavouriteMapper;
import com.acme.webserviceserentcar.favourite.resource.CreateFavouriteResource;
import com.acme.webserviceserentcar.favourite.resource.FavouriteResource;
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
@RequestMapping("/api/v1/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final ClientService clientService;
    private final FavouriteMapper mapper;

    public FavouriteController(ClientService clientService, FavouriteService favouriteService, FavouriteMapper mapper) {
        this.favouriteService = favouriteService;
        this.clientService = clientService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get All Favourites", description = "Get All Favourites", tags = {"Favourites"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Favourites returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FavouriteResource.class))
                    ))
    })
    @GetMapping
    public Page<FavouriteResource> getAllFavourite(Pageable pageable) {
        return mapper.modelListToPage(favouriteService.getAll(), pageable);
    }

    @Operation(summary = "Get Favourites by Id", description = "Get Favourite by Id", tags = {"Favourites"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FavouriteResource.class)
                    ))
    })
    @GetMapping("{favouriteId}")
    public FavouriteResource getFavouriteById(@PathVariable Long favouriteId) {
        return mapper.toResource(favouriteService.getById(favouriteId));
    }

    @Operation(summary = "Get All Favourites by Client id", description = "Get All Favourites by Client id", tags = {"Favourites"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Favourites of Client returned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FavouriteResource.class))
                    ))
    })
    @GetMapping("client/{clientId}")
    public Page<FavouriteResource> getAllFavouritesByClientId(@PathVariable Long clientId, Pageable pageable) {
        return favouriteService.getAllFavouritesByClientId(clientId, pageable).map(mapper::toResource);
    }


    @Operation(summary = "Create Favourite", description = "Create Favourite", tags = {"Favourites"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FavouriteResource.class)
                    ))
    })
    @PostMapping("client/{clientId}/car/{carId}")
    public FavouriteResource createFavourite(@PathVariable Long clientId,
                                             @PathVariable Long carId,
                                             @Valid @RequestBody CreateFavouriteResource request) {
        return mapper.toResource(favouriteService.create(clientId, carId, mapper.toModel(request)));
    }

    @Operation(summary = "Delete Favourite", description = "Delete Favourite", tags = {"Favourites"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FavouriteResource.class)
                    ))
    })
    @DeleteMapping("{favouriteId}")
    public ResponseEntity<?> deleteFavourite(@PathVariable Long favouriteId) {
        return favouriteService.delete(favouriteId); }
}
