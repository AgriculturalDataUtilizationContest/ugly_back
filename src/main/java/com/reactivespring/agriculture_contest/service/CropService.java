package com.reactivespring.agriculture_contest.service;

import com.reactivespring.agriculture_contest.dto.CropDto;

public interface CropService {
    CropDto.ForecastResDto  getForecastCropDetails(CropDto.ForecastReq forecastReq);

    CropDto.BaseRes getBaseCrops(CropDto.BaseReq baseReq);
    CropDto.PastUglyRes getPastUgly(Integer grainId);

    CropDto.predictionRes predictionPast(CropDto.predictionReq pastUglyReq);
}
