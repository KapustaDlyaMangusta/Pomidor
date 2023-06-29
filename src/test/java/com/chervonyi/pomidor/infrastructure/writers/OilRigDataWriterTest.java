package com.chervonyi.pomidor.infrastructure.writers;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.infrastructure.BaseInfrastructureTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OilRigDataWriterTest extends BaseInfrastructureTest {

    private OilRigDataWriter oilRigDataWriter;

    @BeforeEach
    public void setUp() {
        oilRigDataWriter = new OilRigDataWriter();

        ReflectionTestUtils.setField(oilRigDataWriter, "dataDirectory", TEST_FILE_PATH);
    }

    @Test
    public void testWriteToCsv() throws IOException {
        Map<UUID, OilRig> oilRigMap = new HashMap<>();
        var oilRigId = UUID.randomUUID();
        var oilRig = new OilRig(oilRigId, FillingDegree.FULL, 10.0, 20.0, new ArrayList<>());
        oilRigMap.put(oilRigId, oilRig);

        oilRigDataWriter.writeToCsv(oilRigMap);

        var expectedFilePath = TEST_FILE_PATH
                + "/oilRig-"
                + LocalDate.now()
                    .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd"))
                + ".csv";

        var csvFile = new File(expectedFilePath);
        assertTrue(csvFile.exists());

        var expectedFilePathA = Paths.get(expectedFilePath);
        var lines = Files.readAllLines(expectedFilePathA);
        assertEquals(2, lines.size());

        var dataRow = lines.get(1);
        var data = dataRow.split(",");

        assertEquals(oilRigId.toString(), data[0]);
        assertEquals(FillingDegree.FULL.toString(), data[1]);
        assertEquals("20.0", data[2]);
        assertEquals("10.0", data[3]);

        Files.deleteIfExists(expectedFilePathA);
    }
}