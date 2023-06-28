package com.chervonyi.pomidor.infrastructure.data;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.models.OilRig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataContext {

    @Getter
    private final Map<Integer, OilRig> entityMap = new HashMap<>();

    @Value("${data.directory}")
    private String dataDirectory;

    @PostConstruct
    public void initializeData() {
        var currentDate = LocalDate.now();
        var currentMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        try {
            Files.list(Paths.get(dataDirectory))
                    .filter(path -> path.getFileName().toString().startsWith("oilRig-" + currentMonth))
                    .forEach(this::loadCsvData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCsvData(Path filePath) {
        try {
            var lines = Files.readAllLines(filePath);

            for (var i = 1; i < lines.size(); i++) {
                var data = lines.get(i).split(",");

                var id = Integer.parseInt(data[0]);
                var rigFillingDegree = Enum.valueOf(FillingDegree.class, data[1]);
                var longitude = Double.parseDouble(data[2]);
                var latitude = Double.parseDouble(data[3]);

                var entity = new OilRig(id, rigFillingDegree, latitude, longitude, new ArrayList<>());
                entityMap.put(id, entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
