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
//        ArrayList<CropDto.ForecastCropInfo> forecastResDtos = new ArrayList<>();
//        String category = forecastReq.getCategory();
//        // 이 부분 배웠다..! 오..?
//        cropRepository.findByCategory(category).forEach(crop -> {
//            forecastResDtos.add(CropDto.ForecastCropInfo.builder()
//                    .cropsName(crop.getCropKorName())
//                    .cropsImage(crop.getCropsImage())
//                    .build());
//        });
//
//        CropDto.ForecastResDto temp = CropDto.ForecastResDto.builder().crops(forecastResDtos).build();
//        temp.setCrops(forecastResDtos);
//        return temp;

        CropDto.ForecastResDto forecastResDtos = CropDto.ForecastResDto.builder().crops(new ArrayList<>()).build();

        cropRepository.findByCategory(forecastReq.getCategory()).forEach(crop -> {
            forecastResDtos.crops.add(CropDto.ForecastCropInfo.builder()
                    .cropsName(crop.getCropKorName())
                    .cropsImage(crop.getCropsImage())
                    .build());
        });

        return forecastResDtos;
    }

}
