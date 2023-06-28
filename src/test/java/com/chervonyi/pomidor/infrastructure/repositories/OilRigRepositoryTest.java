package com.chervonyi.pomidor.infrastructure.repositories;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.errors.OilRigErrors;
import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.domain.shared.ApplicationError;
import com.chervonyi.pomidor.infrastructure.BaseInfrastructureTest;
import com.chervonyi.pomidor.infrastructure.context.DataContext;
import com.chervonyi.pomidor.infrastructure.writers.OilRigDataWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

class OilRigRepositoryTest extends BaseInfrastructureTest {

    @Mock
    private DataContext dataContext;

    @Mock
    private OilRigDataWriter oilRigDataWriter;

    @InjectMocks
    private OilRigRepository oilRigRepository;

    private final List<OilRig> defaultOilRigs = List.of(
            new OilRig(UUID.randomUUID(), FillingDegree.EMPTY, 52.123, 4.567, new ArrayList<>()),
            new OilRig(UUID.randomUUID(), FillingDegree.EMPTY, 51.234, 3.456, new ArrayList<>()),
            new OilRig(UUID.randomUUID(), FillingDegree.FULL, 50.345, 2.345, new ArrayList<>()));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(oilRigDataWriter, "dataDirectory", TEST_FILE_PATH);
        ReflectionTestUtils.setField(dataContext, "dataDirectory", TEST_FILE_PATH);
    }

    @Test
    void testGetOilRigs_ReturnsAllOilRigs() {
        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        for (var oilRig: defaultOilRigs) {
            oilRigMap.put(oilRig.getId(), oilRig);
        };

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.getOilRigs();

        assertTrue(result.isSuccess());

        var oilRigs = result.getValue();

        assertEquals(defaultOilRigs.size(), oilRigs.size());

        for (var oilRig: defaultOilRigs) {
            assertTrue(oilRigs.contains(oilRig));
        }

        verify(dataContext).getOilRigMap();
    }


    @Test
    void testGetOilRig_ReturnsOilRigById(){
        var oilRig = defaultOilRigs.get(0);

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        oilRigMap.put(oilRig.getId(), oilRig);

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.getOilRig(oilRig.getId());

        assertTrue(result.isSuccess());

        assertEquals(oilRig, result.getValue());

        verify(dataContext).getOilRigMap();
    }

    @Test
    void testGetOilRig_NotFound() {
        var oilRig = defaultOilRigs.get(0);

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        oilRigMap.put(defaultOilRigs.get(1).getId(), defaultOilRigs.get(1));

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.getOilRig(oilRig.getId());

        assertFalse(result.isSuccess());

        assertEquals(OilRigErrors.OIL_RIG_NOT_FOUND_BY_ID_ERROR, result.error());

        verify(dataContext).getOilRigMap();
    }

    @Test
    void testCreateOilRig_Success() {
        var oilRig = new OilRig(null, FillingDegree.EMPTY, 52.123, 4.567, new ArrayList<>());

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.createOilRig(oilRig);

        assertTrue(result.isSuccess());
        assertNotNull(oilRig.getId());
        assertEquals(oilRig, oilRigMap.get(oilRig.getId()));

        verify(oilRigDataWriter, times(1)).writeToCsv(oilRigMap);
    }

    @Test
    void testCreateOilRig_Error() {
        var oilRig = new OilRig(null, FillingDegree.EMPTY, 52.123, 4.567, new ArrayList<>());

        when(dataContext.getOilRigMap()).thenThrow(new RuntimeException("Something went wrong"));

        var result = oilRigRepository.createOilRig(oilRig);

        assertFalse(result.isSuccess());

        assertEquals("OilRig.CreateError", result.error().code());

        verify(oilRigDataWriter, never()).writeToCsv(anyMap());
    }

    @Test
    public void testAddShipmentDateToOilRigById_Success() {
        var oilRig = defaultOilRigs.get(0);
        var shipmentDate = new Date();

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        oilRigMap.put(oilRig.getId(), oilRig);

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.addShipmentDateToOilRigById(oilRig.getId(), shipmentDate);

        assertTrue(result.isSuccess());
        assertTrue(oilRig.getShipmentByTankerDates().contains(shipmentDate));

        verify(oilRigDataWriter, times(1)).writeToCsv(oilRigMap);
    }

    @Test
    public void testAddShipmentDateToOilRigById_OilRigNotFound() {
        Map<UUID, OilRig> oilRigMap = new HashMap<>();

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.addShipmentDateToOilRigById(UUID.randomUUID(), new Date());

        assertFalse(result.isSuccess());
        assertEquals(OilRigErrors.OIL_RIG_NOT_FOUND_BY_ID_ERROR, result.error());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }

    @Test
    public void testAddShipmentDateToOilRigById_Error() {
        var oilRigId = UUID.randomUUID();
        var shipmentDate = new Date();
        when(dataContext.getOilRigMap()).thenThrow(new RuntimeException("Something went wrong"));

        var result = oilRigRepository.addShipmentDateToOilRigById(oilRigId, shipmentDate);

        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.error().message());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }

    @Test
    public void testDeleteOilRig_Success() {
        var oilRig = defaultOilRigs.get(0);

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        oilRigMap.put(oilRig.getId(), oilRig);

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.deleteOilRig(oilRig.getId());

        assertTrue(result.isSuccess());
        assertFalse(oilRigMap.containsKey(oilRig.getId()));

        verify(oilRigDataWriter, times(1)).writeToCsv(oilRigMap);
    }

    @Test
    public void testDeleteOilRig_OilRigNotFound() {
        Map<UUID, OilRig> oilRigMap = new HashMap<>();

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var result = oilRigRepository.deleteOilRig(UUID.randomUUID());

        assertFalse(result.isSuccess());
        assertEquals(OilRigErrors.OIL_RIG_NOT_FOUND_BY_ID_ERROR, result.error());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }

    @Test
    public void testDeleteOilRig_Error() {
        when(dataContext.getOilRigMap()).thenThrow(new RuntimeException("Something went wrong"));

        var result = oilRigRepository.deleteOilRig(UUID.randomUUID());

        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.error().message());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }

    @Test
    public void testUpdateOilRig_Success() {
        Map<UUID, OilRig> oilRigMap = new HashMap<>();

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var updatedOilRig = new OilRig();
        updatedOilRig.setFillingDegree(FillingDegree.FULL);
        updatedOilRig.setLatitude(123.456);
        updatedOilRig.setLongitude(987.654);
        updatedOilRig.setShipmentByTankerDates(new ArrayList<>());

        var result = oilRigRepository.updateOilRig(UUID.randomUUID(), updatedOilRig);

        assertFalse(result.isSuccess());
        assertEquals(OilRigErrors.OIL_RIG_NOT_FOUND_BY_ID_ERROR, result.error());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }

    @Test
    public void testUpdateOilRig_NotFound() {
        var oilRig = defaultOilRigs.get(0);

        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        oilRigMap.put(oilRig.getId(), oilRig);

        when(dataContext.getOilRigMap()).thenReturn(oilRigMap);

        var updatedOilRig = new OilRig();
        updatedOilRig.setFillingDegree(FillingDegree.FULL);
        updatedOilRig.setLatitude(123.456);
        updatedOilRig.setLongitude(987.654);
        updatedOilRig.setShipmentByTankerDates(new ArrayList<>());

        var result = oilRigRepository.updateOilRig(oilRig.getId(), updatedOilRig);

        assertTrue(result.isSuccess());
        assertEquals(updatedOilRig.getFillingDegree(), oilRig.getFillingDegree());
        assertEquals(updatedOilRig.getLatitude(), oilRig.getLatitude());
        assertEquals(updatedOilRig.getLongitude(), oilRig.getLongitude());
        assertEquals(updatedOilRig.getShipmentByTankerDates(), oilRig.getShipmentByTankerDates());

        verify(oilRigDataWriter, times(1)).writeToCsv(oilRigMap);
    }

    @Test
    public void testUpdateOilRig_Error() {
        when(dataContext.getOilRigMap()).thenThrow(new RuntimeException("Something went wrong"));

        var result = oilRigRepository.updateOilRig(UUID.randomUUID(), null);

        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.error().message());

        verify(oilRigDataWriter, never()).writeToCsv(any());
    }
}