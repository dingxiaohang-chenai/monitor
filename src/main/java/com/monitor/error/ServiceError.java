package com.monitor.error;

public class ServiceError extends BaseServerError {

    private ServiceError(Integer code) {
        super(code);
    }

    public static final ServiceError INTERNAL_ERROR = new ServiceError(-1);
    public static final ServiceError BIND_CID_ERROR = new ServiceError(8201);
}
