package com.chervonyi.pomidor.application.services;

import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.domain.shared.Result;
import com.chervonyi.pomidor.domain.shared.ValueResult;
import com.chervonyi.pomidor.infrastructure.repositories.OilRigRepository;
import com.chervonyi.pomidor.presentation.contracts.AddShipmentToOilRigByIdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public final class OilRigService {
    private final OilRigRepository oilRigRepository;

    @Autowired
    public OilRigService(final OilRigRepository oilRigRepository) {
        this.oilRigRepository = oilRigRepository;
    }

    public ValueResult<List<OilRig>> getOilRigs() {
        return oilRigRepository.getOilRigs();
    }

    public ValueResult<OilRig> getOilRig(final UUID id) {
        return oilRigRepository.getOilRig(id);
    }

    public Result createOilRig(final OilRig oilRig) {
        return oilRigRepository.createOilRig(oilRig);
    }

    public Result addShipmentDateToOilRigById(
            final UUID id,
            final AddShipmentToOilRigByIdRequest request) {
        return oilRigRepository.addShipmentDateToOilRigById(
                id,
                request.shipmentDate);
    }

    public Result deleteOilRig(final UUID id) {
        return oilRigRepository.deleteOilRig(id);
    }

    public Result updateOilRig(final UUID id, final OilRig updatedOilRig) {
        return oilRigRepository.updateOilRig(id, updatedOilRig);
    }
}
