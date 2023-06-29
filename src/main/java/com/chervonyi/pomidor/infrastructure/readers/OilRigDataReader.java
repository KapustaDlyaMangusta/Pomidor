package com.chervonyi.pomidor.infrastructure.readers;

import com.chervonyi.pomidor.domain.enums.FillingDegree;
import com.chervonyi.pomidor.domain.models.OilRig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public final class OilRigDataReader {

    public List<OilRig> readFromCsv(final List<Path> filePaths) {
        List<OilRig> oilRigs = new ArrayList<>();

            for (var filePath: filePaths) {
                try {
                    var lines = Files.readAllLines(filePath);
                    for (var i = 1; i < lines.size(); i++) {
                        var data = lines.get(i).split(",");

                        var id = UUID.fromString(data[0]);
                        var rigFillingDegree = Enum.valueOf(
                                                FillingDegree.class,
                                                data[1]);
                        var longitude = Double.parseDouble(data[2]);
                        var latitude = Double.parseDouble(data[3]);
                        var rigShipmentByTankerDates =
                                parseOilRigShipmentByTankerDates(data[4]);

                        var entity = new OilRig(
                                id,
                                rigFillingDegree,
                                latitude,
                                longitude,
                                rigShipmentByTankerDates);

                        oilRigs.add(entity);
                    }
                } catch (IOException e) {
                    log.error(
                            "Error occurred in OilRigDataReader "
                            + "with message {}",
                            e.getMessage());
                }
            }

        return oilRigs;
    }

    private List<Date> parseOilRigShipmentByTankerDates(
            final String datesString) {
        List<Date> dates = new ArrayList<>();
        var dateStrings = datesString.split(";");

        for (var dateString : dateStrings) {
            var date = new Date(
                    Long.parseLong(dateString));
            dates.add(date);
        }

        return dates;
    }
}
