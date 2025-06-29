package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CropController {

    private final CropService cropService;

    @GetMapping("/forecast/{category}")
    public ResponseEntity<CropDto.ForecastResDto> forecast(@PathVariable String category) {
        CropDto.ForecastReq forecastReq = new CropDto.ForecastReq(category);
        return ResponseEntity.ok().body(cropService.getForecastCropDetails(forecastReq));
    }

    @GetMapping("/base/{cropName}")
    public ResponseEntity<CropDto.BaseRes> base(@PathVariable String cropName) {
        CropDto.BaseReq baseReq = new CropDto.BaseReq(cropName);
        return ResponseEntity.ok().body(cropService.getBaseCrops(baseReq));
    }

    @GetMapping("/prediction/past/{cropName}")
    public ResponseEntity<CropDto.predictionRes> predictionPast(@PathVariable String cropName) {
        CropDto.predictionReq pastUglyReq = new CropDto.predictionReq(cropName);
        return ResponseEntity.ok().body(cropService.predictionPast(pastUglyReq));
    }

    @GetMapping("/prediction/future/{cropName}")
    public ResponseEntity<CropDto.predictionRes> predictionFuture(@PathVariable String cropName) {
        CropDto.predictionReq pastUglyReq = new CropDto.predictionReq(cropName);
        return ResponseEntity.ok().body(cropService.predictionFuture(pastUglyReq));
    }

    @GetMapping("/recommendation/{cropName}")
    public ResponseEntity<CropDto.recommendationRes> recommendation(@PathVariable String cropName) {
        CropDto.predictionReq recommendationReq = new CropDto.predictionReq(cropName);
        return ResponseEntity.ok().body(cropService.recommendation(recommendationReq));
    }

    @GetMapping("/issue/{cropName}")
    public ResponseEntity<CropDto.issueCheckRes> issueCheck(@PathVariable String cropName) {
        CropDto.predictionReq issueCheckReq = new CropDto.predictionReq(cropName);
        return ResponseEntity.ok().body(cropService.issueCheck(issueCheckReq));
    }

    @GetMapping("/comparison/price/{cropName}")
    public ResponseEntity<CropDto.comparisonPriceRes> comparisonPrice(@PathVariable String cropName) {
        CropDto.predictionReq comparisonPriceReq = new CropDto.predictionReq(cropName);
        return ResponseEntity.ok().body(cropService.comparePrice(comparisonPriceReq));
    }

    @GetMapping("/comparison/category/{category}")
    public ResponseEntity<CropDto.comparisonCategoryRes> comparisonCategory(@PathVariable String category) {
        CropDto.ForecastReq comparisonCategoryReq = new CropDto.ForecastReq(category);
        return ResponseEntity.ok().body(cropService.compareCategory(comparisonCategoryReq));
    }
}
