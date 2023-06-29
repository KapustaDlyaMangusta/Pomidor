package com.chervonyi.pomidor.presentation.controllers;

import com.chervonyi.pomidor.application.services.OilRigService;
import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.presentation.contracts.AddShipmentToOilRigByIdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/oilRigs")
@RestController
public final class OilRigController {
    private final OilRigService oilRigService;

    @Autowired
    public OilRigController(final OilRigService oilRigService) {
        this.oilRigService = oilRigService;
    }

    @GetMapping
    public ResponseEntity<Object> getOilRigs() {
         var result = oilRigService.getOilRigs();

         return result.isSuccess()
                 ? ResponseEntity.ok(result.getValue())
                 : ResponseEntity.badRequest().body(result.error());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getOilRig(
            @PathVariable("id") final UUID id) {
        var result = oilRigService.getOilRig(id);

        return result.isSuccess()
                ? ResponseEntity.ok(result.getValue())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.error());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createOilRig(
            @RequestBody final OilRig oilRig) {
        var result = oilRigService.createOilRig(oilRig);

        return result.isSuccess()
                ? ResponseEntity.ok(null)
                : ResponseEntity.badRequest()
                    .body(result.error());
    }

    @PostMapping(
            path = "/{id}/shipments/add",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addShipmentDateToOilRigById(
            @PathVariable final UUID id,
            @RequestBody final AddShipmentToOilRigByIdRequest request) {
        var result = oilRigService.addShipmentDateToOilRigById(id, request);

        return result.isSuccess()
                ? ResponseEntity.ok(null)
                : ResponseEntity.badRequest()
                    .body(result.error());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteOilRig(
            @PathVariable("id") final UUID id) {
        var result = oilRigService.deleteOilRig(id);

        return result.isSuccess()
                ? ResponseEntity.ok(null)
                : ResponseEntity.badRequest()
                    .body(result.error());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateOilRig(
            @PathVariable("id") final UUID id,
            @RequestBody final OilRig oilRig) {
        var result = oilRigService.updateOilRig(id, oilRig);

        return result.isSuccess()
                ? ResponseEntity.ok(null)
                : ResponseEntity.badRequest()
                    .body(result.error());
    }
}
