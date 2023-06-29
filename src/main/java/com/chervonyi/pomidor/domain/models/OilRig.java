package com.chervonyi.pomidor.domain.models;

import com.chervonyi.pomidor.domain.enums.FillingDegree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OilRig {
    private UUID id;
    private FillingDegree fillingDegree;
    private double longitude;
    private double latitude;
    private List<Date> shipmentByTankerDates;
}
