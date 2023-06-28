package com.chervonyi.pomidor.infrastructure.writers;

import com.chervonyi.pomidor.domain.models.OilRig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public final class OilRigDataWriter {
    @Value("${data.directory}")
    private String dataDirectory;

    public void writeToCsv(final Map<UUID, OilRig> oilRigMap) {
        var csvFilePath = Paths.get(
                dataDirectory
                + "/oilRig-"
                + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + ".csv").toFile();

        try (var writer = new BufferedWriter(new FileWriter(
                csvFilePath,
                false))) {
            var csvHeader = "Id, FillingDegree, Longitude,"
                    + " Latitude, ShipmentByTankerDates";

            writer.write(csvHeader);
            writer.newLine();

            for (var oilRig : oilRigMap.values()) {
                writer.write(
                        oilRig.getId() + ","
                        + oilRig.getFillingDegree()
                        + "," + oilRig.getLatitude()
                        + "," + oilRig.getLongitude() + ","
                        + oilRigShipmentByTankerDatesToCsvFormat(
                                oilRig.getShipmentByTankerDates()));

                writer.newLine();
            }
        } catch (IOException e) {
            log.error(
                    "Error occurred "
                    + "in OilRigDataWriter with message {}",
                    e.getMessage());
        }
    }

    private String oilRigShipmentByTankerDatesToCsvFormat(
            final List<Date> shipmentByTankerDates) {
        var builder = new StringBuilder();
        for (int i = 0; i < shipmentByTankerDates.size(); i++) {
            var date = shipmentByTankerDates.get(i);
            builder.append(date.getTime());
            if (i < shipmentByTankerDates.size() - 1) {
                builder.append(";");
            }
        }

        return builder.toString();
    }
}
