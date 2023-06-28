package com.chervonyi.pomidor.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Error {
    private String code;
    private String message;

    public static final Error NONE = new Error("","");
    public static final Error NULL_VALUE =  new Error("Error.NullValue", "The specified result value is null.");

    @Override
    public String toString() {
        return code;
    }
}
