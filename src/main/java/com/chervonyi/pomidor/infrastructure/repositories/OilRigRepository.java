package com.chervonyi.pomidor.infrastructure.repositories;

import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.domain.shared.ApplicationError;
import com.chervonyi.pomidor.domain.shared.Result;
import com.chervonyi.pomidor.domain.shared.ValueResult;
import com.chervonyi.pomidor.infrastructure.context.DataContext;
import com.chervonyi.pomidor.infrastructure.writers.OilRigDataWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.chervonyi.pomidor.domain.errors.OilRigErrors.OIL_RIG_NOT_FOUND_BY_ID_ERROR;

@Slf4j
@Repository
public final class OilRigRepository {

    private final DataContext dataContext;
    private final OilRigDataWriter oilRigDataWriter;

    @Autowired
    public OilRigRepository(
            final DataContext dataContext,
            final OilRigDataWriter oilRigDataWriter) {
        this.dataContext = dataContext;
        this.oilRigDataWriter = oilRigDataWriter;
    }

    public ValueResult<List<OilRig>> getOilRigs() {
        return ValueResult.of(
                new LinkedList<>(
                        dataContext.getOilRigMap()
                                .values()));
    }

    public ValueResult<OilRig> getOilRig(final UUID id) {
        return ValueResult.of(
                dataContext.getOilRigMap()
                        .get(id),
                OIL_RIG_NOT_FOUND_BY_ID_ERROR);
    }

    public Result createOilRig(final OilRig oilRig) {
        try {
            oilRig.setId(UUID.randomUUID());
            dataContext.getOilRigMap()
                    .put(oilRig.getId(), oilRig);

            saveChanges();

            return Result.success();
        } catch (Exception e) {
            log.error(
                    "Error occurred in OilRigRepository "
                    + "in createOilRig with message {}",
                    e.getMessage());

            return Result.failure(new ApplicationError(
                    "OilRig.CreateError",
                    e.getMessage()));
        }
    }

    public Result addShipmentDateToOilRigById(
            final UUID id,
            final Date shipmentDate) {
        try {
            var allOilRigs = dataContext.getOilRigMap();

            for (var oilRig : allOilRigs.values()) {
                if (oilRig.getId().toString().equals(id.toString())) {
                    oilRig.getShipmentByTankerDates().add(shipmentDate);

                    saveChanges();

                    return Result.success();
                }
            }

            return Result.failure(OIL_RIG_NOT_FOUND_BY_ID_ERROR);
        } catch (Exception e) {
            log.error(
                    "Error occurred in OilRigRepository "
                    + "in addShipmentDateToOilRigById with message {}",
                    e.getMessage());

            return Result.failure(
                    new ApplicationError(
                            "OilRig.AddShipmentDateToOilRigByIdError",
                            e.getMessage()));
        }
    }

    public Result deleteOilRig(final UUID id) {
        try {
            var result = dataContext.getOilRigMap()
                    .remove(id) != null
                    ? Result.success()
                    : Result.failure(OIL_RIG_NOT_FOUND_BY_ID_ERROR);

            if (result.isSuccess()) {
                saveChanges();
            }

            return result;
        } catch (Exception e) {
            log.error(
                    "Error occurred in OilRigRepository"
                    + " in deleteOilRig with message {}",
                    e.getMessage());

            return Result.failure(
                    new ApplicationError(
                            "OilRig.DeleteOilRigError",
                            e.getMessage()));
        }
    }

    public Result updateOilRig(
            final UUID id,
            final OilRig updatedOilRig) {
        try {
            var oilRig = dataContext.getOilRigMap().get(id);

            if (oilRig == null) {
                return Result.failure(OIL_RIG_NOT_FOUND_BY_ID_ERROR);
            }

            oilRig.setFillingDegree(updatedOilRig.getFillingDegree());
            oilRig.setLatitude(updatedOilRig.getLatitude());
            oilRig.setLongitude(updatedOilRig.getLongitude());
            oilRig.setShipmentByTankerDates(updatedOilRig
                    .getShipmentByTankerDates());

            saveChanges();

            return Result.success();
        } catch (Exception e) {
            log.error(
                    "Error occurred in OilRigRepository"
                    + " in updateOilRig with message {}",
                    e.getMessage());

            return Result.failure(
                    new ApplicationError(
                            "OilRig.UpdateOilRigError",
                            e.getMessage()));
        }
    }

    private void saveChanges() {
        oilRigDataWriter.writeToCsv(dataContext.getOilRigMap());
    }
}
