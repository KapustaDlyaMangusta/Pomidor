package com.chervonyi.pomidor.infrastructure.context;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.infrastructure.BaseInfrastructureTest;
import com.chervonyi.pomidor.infrastructure.readers.OilRigDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class DataContextTest extends BaseInfrastructureTest {

    @Mock
    private OilRigDataReader oilRigDataReader;

    @InjectMocks
    private DataContext dataContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(dataContext, "dataDirectory", TEST_FILE_PATH);
    }

    @Test
    public void testInitializeData() throws Exception {
        var oilRig1 = new OilRig(UUID.randomUUID(), FillingDegree.EMPTY, 1.0, 2.0, new ArrayList<>());
        var oilRig2 = new OilRig(UUID.randomUUID(), FillingDegree.FULL, 3.0, 4.0, new ArrayList<>());

        when(oilRigDataReader.readFromCsv(
                Files.list(
                    Paths.get(TEST_FILE_PATH))
                        .filter(path -> path.getFileName()
                            .toString()
                            .contains("oilRig-"
                                + LocalDate.now().format(
                                DateTimeFormatter.ofPattern("yyyy-MM"))))
                        .toList()))
        .thenReturn(List.of(oilRig1, oilRig2));

        dataContext.initializeData();

        assertEquals(2, dataContext.getOilRigMap().size());
        assertTrue(dataContext.getOilRigMap().containsKey(oilRig1.getId()));
        assertTrue(dataContext.getOilRigMap().containsKey(oilRig2.getId()));
    }
}