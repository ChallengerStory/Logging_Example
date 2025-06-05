package com.aicube.log_proj;

import io.micrometer.core.annotation.Timed;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    @Timed(
            value = "order_processing_time",
            histogram = true,
            percentiles = {0.5, 0.95},
            serviceLevelObjectives = {5, 10, 50, 200, 1000}
    )
    public ResponseEntity<?> test(
    ) {
        String result = testService.findInfo();
        return ResponseEntity.ok(result);
    }
}