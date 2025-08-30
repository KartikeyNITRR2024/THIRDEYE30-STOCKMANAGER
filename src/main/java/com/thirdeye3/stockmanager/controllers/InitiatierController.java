package com.thirdeye3.stockmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.utils.Initiatier;

@RestController
@RequestMapping("/api/updateinitiatier")
public class InitiatierController {

    private static final Logger logger = LoggerFactory.getLogger(InitiatierController.class);

    @Value("${thirdeye.uniqueId}")
    private Integer uniqueId;

    @Value("${thirdeye.uniqueCode}")
    private String uniqueCode;

    @Autowired
    private Initiatier initiatier;

    @GetMapping("/{id}/{code}")
    public ResponseEntity<Response<String>> getStatus(@PathVariable Integer id, @PathVariable String code) {
        if (!id.equals(uniqueId) || !code.equals(uniqueCode)) {
            return buildResponse(false, 404, "Invalid credentials", null);
        }
        try {
            initiatier.init();
            return buildResponse(true, 0, null, "Valid credentials");
        } catch (Exception ex) {
            logger.error("Init failed: {}", ex.getMessage(), ex);
            return buildResponse(false, 500, "Something went wrong", null);
        }
    }

    private ResponseEntity<Response<String>> buildResponse(boolean success, int errorCode, String errorMessage, String body) {
        return ResponseEntity
                .status(success ? 200 : errorCode)
                .body(new Response<>(success, errorCode, errorMessage, body));
    }
}
