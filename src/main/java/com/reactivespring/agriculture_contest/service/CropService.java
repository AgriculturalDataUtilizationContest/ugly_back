package com.reactivespring.agriculture_contest.service;

import com.reactivespring.agriculture_contest.dto.CropDto;

import java.util.ArrayList;

public interface CropService {
    CropDto.ForecastResDto  getForecastCropDetails(CropDto.ForecastReq forecastReq);

    CropDto.BaseRes getBaseCrops(CropDto.BaseReq baseReq);
}
