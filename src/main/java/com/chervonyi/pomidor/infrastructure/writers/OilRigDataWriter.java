package com.chervonyi.pomidor.infrastructure.managers;

import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.infrastructure.context.DataContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class OilRigDataManager {
    private final DataContext dataContext;

    @Autowired
    public OilRigDataManager(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public void updateOilRig(int id, OilRig updatedOilRig) {
        var allOilRigs = dataContext.getOilRigMap();

        for (var oilRig : allOilRigs.values()) {
            if (oilRig.getId() == id) {
                oilRig.setFillingDegree(updatedOilRig.getFillingDegree());
                oilRig.setLatitude(updatedOilRig.getLatitude());
                oilRig.setLongitude(updatedOilRig.getLongitude());
                oilRig.setShipmentByTankerDates(updatedOilRig.getShipmentByTankerDates());

                break;
            }
        }

        writeToCsv(allOilRigs);
    }

    private void writeToCsv(Map<Integer, OilRig> entities) {
        var csvFilePath = dataContext.getDataDirectory();
        var tempFilePath = csvFilePath + ".tmp";

        try (var writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            for (var oilRig : entities.values()) {
                // Write each OilRig as a line in the CSV file
                writer.write(oilRig.getId() + "," + oilRig.getFillingDegree() + "," +
                        oilRig.getLatitude() + "," + oilRig.getLongitude() + "," + oilRig.getShipmentByTankerDates());
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }


        try {
            Files.move(Path.of(tempFilePath), Path.of(csvFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }

    //private String oilRigShipmentByTankerDatesToCsvFormat(List<Date> shipmentByTankerDates){
      //  var result
    //}
}
