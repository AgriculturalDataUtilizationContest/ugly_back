package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.reactivespring.agriculture_contest.service.CropService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;

    @Override
    public CropDto.ForecastResDto getForecastCropDetails(CropDto.ForecastReq forecastReq) {
        CropDto.ForecastResDto forecastResDtos = CropDto.ForecastResDto.builder().crops(new ArrayList<>()).build();

        cropRepository.findByCategory(forecastReq.getCategory()).forEach(crop -> {
            forecastResDtos.crops.add(CropDto.ForecastCropInfo.builder()
                    .cropsName(crop.getCropKorName())
                    .cropsImage(crop.getCropsImage())
                    .build());
        });

        return forecastResDtos;
    }

    @Override
    public CropDto.ForecastResDto getBaseCrops() {
        return null;
    }

}
