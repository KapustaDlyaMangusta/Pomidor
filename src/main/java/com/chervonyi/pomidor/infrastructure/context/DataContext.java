package com.chervonyi.pomidor.infrastructure.context;

import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.infrastructure.readers.OilRigDataReader;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public final class DataContext {

    private final OilRigDataReader oilRigDataReader;

    @Getter
    private final Map<UUID, OilRig> oilRigMap = new HashMap<>();

    @Value("${data.directory}")
    private String dataDirectory;

    @Autowired
    public DataContext(final OilRigDataReader oilRigDataReader) {
        this.oilRigDataReader = oilRigDataReader;
    }

    @PostConstruct
    public void initializeData() {
        var currentDate = LocalDate.now();
        var currentMonth = currentDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM"));

        try {
            oilRigMap.clear();

            var paths = Files.list(Paths.get(dataDirectory))
                            .filter(path -> path.getFileName()
                                    .toString()
                                    .contains("oilRig-" + currentMonth))
                            .toList();

            var oilRigsFromFiles = oilRigDataReader.readFromCsv(paths);

            oilRigsFromFiles.forEach(oilRig ->
                    oilRigMap.put(oilRig.getId(), oilRig));
        } catch (IOException e) {
            log.error(
                    "Error occurred in DataContext "
                    + "in initializeDataMethod with message {}",
                    e.getMessage());
        }
    }
}
