package com.chervonyi.pomidor.domain.shared;

public record Result(
        boolean isSuccess,
        ApplicationError error) {
    public static Result success() {
        return new Result(true, ApplicationError.NONE);
    }

    public static Result failure(final ApplicationError error) {
        return new Result(false, error);
    }
}


