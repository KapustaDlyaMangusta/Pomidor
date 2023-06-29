package com.chervonyi.pomidor.presentation.controllers;

import com.chervonyi.pomidor.application.services.OilRigService;
import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.domain.shared.ApplicationError;
import com.chervonyi.pomidor.domain.shared.Result;
import com.chervonyi.pomidor.domain.shared.ValueResult;
import com.chervonyi.pomidor.presentation.contracts.AddShipmentToOilRigByIdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OilRigControllerTest {

    @Mock
    private OilRigService oilRigService;

    @InjectMocks
    private OilRigController oilRigController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOilRigs_Success() {
        var oilRigs = List.of(
                new OilRig(UUID.randomUUID(), FillingDegree.EMPTY, 50.123, 10.456, new ArrayList<>()),
                new OilRig(UUID.randomUUID(), FillingDegree.FULL, 51.789, 11.234, new ArrayList<>()));

        var successResult = ValueResult.of(oilRigs);

        when(oilRigService.getOilRigs()).thenReturn(successResult);

        var response = oilRigController.getOilRigs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(oilRigs, response.getBody());

        verify(oilRigService, times(1)).getOilRigs();
    }

    @Test
    void testGetOilRigs_Failure() {
        List<OilRig> nullList = null;
        var failureResult = ValueResult.of(nullList);

        when(oilRigService.getOilRigs()).thenReturn(failureResult);

        var response = oilRigController.getOilRigs();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(failureResult.error(), response.getBody());

        verify(oilRigService, times(1)).getOilRigs();
    }

    @Test
    public void testGetOilRig_Success() {
        var id = UUID.randomUUID();
        var expectedOilRig = new OilRig();

        when(oilRigService.getOilRig(id)).thenReturn(ValueResult.of(expectedOilRig));

        var response = oilRigController.getOilRig(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOilRig, response.getBody());
    }

    @Test
    public void testGetOilRig_Failure() {
        var id = UUID.randomUUID();
        when(oilRigService.getOilRig(id)).thenReturn(ValueResult.of(null));

        var response = oilRigController.getOilRig(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ApplicationError.NULL_VALUE, response.getBody());
    }

    @Test
    void testCreateOilRig_Success() {
        var oilRig = new OilRig();

        when(oilRigService.createOilRig(oilRig)).thenReturn(Result.success());

        var response = oilRigController.createOilRig(oilRig);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateOilRig_Failure() {
        var oilRig = new OilRig();
        var error = new ApplicationError(
                "CreationError",
                "Failed to create oil rig");

        when(oilRigService.createOilRig(oilRig)).thenReturn(Result.failure(error));

        var response = oilRigController.createOilRig(oilRig);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(error, response.getBody());
    }

    @Test
    void testAddShipmentDateToOilRigById_Success() {
        var id = UUID.randomUUID();
        var request = new AddShipmentToOilRigByIdRequest(new Date());

        when(oilRigService.addShipmentDateToOilRigById(id, request))
                .thenReturn(Result.success());

        var response = oilRigController.addShipmentDateToOilRigById(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddShipmentDateToOilRigById_Failure() {
        var id = UUID.randomUUID();
        var request = new AddShipmentToOilRigByIdRequest(new Date());
        var error = new ApplicationError(
                "AddShipmentDateToOilRigByIdError",
                "Failed to add oil rig shipment date");

        when(oilRigService.addShipmentDateToOilRigById(id, request))
                .thenReturn(Result.failure(error));

        var response = oilRigController.addShipmentDateToOilRigById(id, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(error, response.getBody());
    }

    @Test
    void testDeleteOilRig_Success() {
        var id = UUID.randomUUID();

        when(oilRigService.deleteOilRig(id))
                .thenReturn(Result.success());

        var response = oilRigController.deleteOilRig(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteOilRig_Failure() {
        var id = UUID.randomUUID();
        var error = new ApplicationError(
                "DeleteError",
                "Failed to delete oil rig");

        when(oilRigService.deleteOilRig(id))
                .thenReturn(Result.failure(error));

        var response = oilRigController.deleteOilRig(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(error, response.getBody());
    }

    @Test
    void testUpdateOilRig_Success() {
        var id = UUID.randomUUID();
        var updatedOilRig = new OilRig();

        when(oilRigService.updateOilRig(id, updatedOilRig))
                .thenReturn(Result.success());

        var response = oilRigController.updateOilRig(id, updatedOilRig);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateOilRig_Failure() {
        var id = UUID.randomUUID();
        var updatedOilRig = new OilRig();
        var error = new ApplicationError(
                "UpdateError",
                "Failed to update oil rig");

        when(oilRigService.updateOilRig(id, updatedOilRig))
                .thenReturn(Result.failure(error));

        var response = oilRigController.updateOilRig(id, updatedOilRig);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(error, response.getBody());
    }
}
