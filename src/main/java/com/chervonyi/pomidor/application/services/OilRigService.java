package com.chervonyi.pomidor.application;

import com.chervonyi.pomidor.domain.models.OilRig;
import com.chervonyi.pomidor.domain.shared.ValueResult;
import com.chervonyi.pomidor.infrastructure.repositories.OilRigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OilRigService {
    private final OilRigRepository oilRigRepository;

    @Autowired
    public OilRigService(OilRigRepository oilRigRepository) {
        this.oilRigRepository = oilRigRepository;
    }

    public ValueResult<List<OilRig>> getOilRigs() {
        return oilRigRepository.getOilRigs();
    }

    public OilRig getOilRig(UUID id) {
        return oilRigRepository.getOilRig(id);
    }

    public boolean createOilRig(OilRig oilRig) {
        return oilRigRepository.createOilRig(oilRig);
    }

    public boolean deleteOilRig(UUID id) {
        return oilRigRepository.deleteOilRig(id);
    }

    public boolean updateOilRig(UUID id, OilRig updatedOilRig) {
        return oilRigRepository.updateOilRig(id, updatedOilRig);
    }

    public boolean addShipmentDateToOilRigById(UUID id, Date shipmentDate){
        return oilRigRepository.addShipmentDateToOilRigById(id, shipmentDate);
    }
}
