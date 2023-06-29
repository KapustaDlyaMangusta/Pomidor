package com.chervonyi.pomidor.domain.errors;

import com.chervonyi.pomidor.domain.shared.ApplicationError;
import org.springframework.http.HttpStatus;

public class OilRigErrors {
    public static final ApplicationError OIL_RIG_NOT_FOUND_BY_ID_ERROR =
            new ApplicationError(
                "OilRig.NotFoundOilRigById",
                "Cannot find oilRig with specified id.");
    }
