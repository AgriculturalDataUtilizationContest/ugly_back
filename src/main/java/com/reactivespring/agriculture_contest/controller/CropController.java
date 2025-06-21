package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CropController {

    private final CropService cropService;

    @GetMapping("/forecast")
    public ResponseEntity<?> forecast(@RequestBody CropDto.ForecastReq forecastReq) {
        cropService.getForecastCropDetails(forecastReq);
        return null;
    }

}
