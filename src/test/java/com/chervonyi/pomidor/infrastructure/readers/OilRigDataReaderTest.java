package com.chervonyi.pomidor.infrastructure.readers;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.infrastructure.BaseInfrastructureTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OilRigDataReaderTest extends BaseInfrastructureTest {
    private static OilRigDataReader oilRigDataReader;

    @BeforeEach
    void setUp() {
        oilRigDataReader = new OilRigDataReader();
    }
    @Test
    void testReadFromCsv() throws IOException {
        var currentMonth = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM"));

        var filePaths = Files.list(Paths.get(TEST_FILE_PATH))
                .filter(path -> path.getFileName()
                        .toString()
                        .contains("oilRig-" + currentMonth))
                .toList();

        var oilRigs = oilRigDataReader.readFromCsv(filePaths);

        assertEquals(2, oilRigs.size());

        var oilRig1 = oilRigs.get(0);
        assertEquals(UUID.fromString("1a811db7-3e34-4d24-9f85-8e1345c60918"), oilRig1.getId());
        assertEquals(FillingDegree.EMPTY, oilRig1.getFillingDegree());
        assertEquals(10.0, oilRig1.getLatitude());
        assertEquals(15.0, oilRig1.getLongitude());
        assertEquals(List.of(new Date(1624982400000L), new Date(1625068800000L)), oilRig1.getShipmentByTankerDates());

        var oilRig2 = oilRigs.get(1);
        assertEquals(UUID.fromString("2a611db7-3e34-4d24-9f85-8e1345c60918"), oilRig2.getId());
        assertEquals(FillingDegree.FULL, oilRig2.getFillingDegree());
        assertEquals(20.0, oilRig2.getLatitude());
        assertEquals(25.0, oilRig2.getLongitude());
        assertEquals(List.of(new Date(1625155200000L), new Date(1625241600000L)), oilRig2.getShipmentByTankerDates());
    }
}