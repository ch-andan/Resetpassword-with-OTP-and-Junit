package com.pacewisdom.admin.utility;
import org.springframework.http.ResponseEntity;

public class CommonUtils {

    public static ResponseEntity<Object> buildResponseEntity(ApiSuccess apiSuccess) {
        return new ResponseEntity<>(apiSuccess, apiSuccess.getStatus());
    }

}
