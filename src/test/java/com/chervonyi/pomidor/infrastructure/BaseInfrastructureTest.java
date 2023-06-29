package com.chervonyi.pomidor.infrastructure;

import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BaseInfrastructureTest {
    protected static final String TEST_FILE_PATH = "E:/JavaProjects/Pomidor/src/test";

    @BeforeAll
    public static void baseSetUp() throws IOException {
        var lines = List.of(
                "Id,FillingDegree,Longitude,Latitude,ShipmentDates",
                "1a811db7-3e34-4d24-9f85-8e1345c60918,EMPTY,10,15,1624982400000;1625068800000",
                "2a611db7-3e34-4d24-9f85-8e1345c60918,FULL,20,25,1625155200000;1625241600000"
        );

        var testFilePath = TEST_FILE_PATH
                + "/oilRig-"
                + LocalDate.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd"))
                + ".csv";

        var file = new File(testFilePath);

        if (!file.exists()) {
            file.createNewFile();
            Files.write(Path.of(testFilePath), lines);
        }
    }
}
