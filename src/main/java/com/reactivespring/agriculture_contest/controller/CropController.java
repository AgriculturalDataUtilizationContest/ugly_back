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
    public ResponseEntity<CropDto.ForecastResDto> forecast(@RequestBody CropDto.ForecastReq forecastReq) {
        return ResponseEntity.ok().body(cropService.getForecastCropDetails(forecastReq));
    }

    @GetMapping("/base")
    public ResponseEntity<CropDto.BaseRes> base(@RequestBody CropDto.BaseReq baseReq) {
        return ResponseEntity.ok().body(cropService.getBaseCrops(baseReq));
    }

    @GetMapping("/prediction/past")
    public ResponseEntity<CropDto.predictionRes> predictionPast(@RequestBody CropDto.predictionReq pastUglyReq) {
        return ResponseEntity.ok().body(cropService.predictionPast(pastUglyReq));
    }

    @GetMapping("/prediction/future")
    public ResponseEntity<CropDto.predictionRes> predictionFuture(@RequestBody CropDto.predictionReq pastUglyReq) {
        return ResponseEntity.ok().body(cropService.predictionFuture(pastUglyReq));
    }

    @GetMapping("/recommendation")
    public ResponseEntity<CropDto.recommendationRes> recommendation(@RequestBody CropDto.predictionReq recommendationReq) {
        return ResponseEntity.ok().body(cropService.recommendation(recommendationReq));
    }

    @GetMapping("/issue")
    public ResponseEntity<CropDto.issueCheckRes> issueCheck(@RequestBody CropDto.predictionReq issueCheckReq) {
        return ResponseEntity.ok().body(cropService.issueCheck(issueCheckReq));
    }

    @GetMapping("/comparison/price")
    public ResponseEntity<CropDto.comparisonPriceRes> comparisonPrice(@RequestBody CropDto.predictionReq comparisonPriceReq) {
        return ResponseEntity.ok().body(cropService.comparePrice(comparisonPriceReq));
    }

    @GetMapping("/comparison/category")
    public ResponseEntity<CropDto.comparisonCategoryRes> comparisonCategory(@RequestBody CropDto.ForecastReq comparisonCategoryReq) {
        return ResponseEntity.ok().body(cropService.compareCategory(comparisonCategoryReq));
    }
}
