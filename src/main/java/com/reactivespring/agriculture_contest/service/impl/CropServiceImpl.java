package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.entity.TbCrop;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import com.reactivespring.agriculture_contest.service.GrainV5Fetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.reactivespring.agriculture_contest.service.CropService;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final GrainV5Fetcher grainV5Fetcher;
    private final RestTemplate restTemplate;

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
    public CropDto.BaseRes getBaseCrops(CropDto.BaseReq baseReq) {

        Integer grainId = cropRepository.findByCropName(baseReq.getCropName()).getCropId();

        CropDto.pastUglyRes pastUglyRes = getPastUgly(grainId);

        CropDto.BaseRes baseRes = CropDto.BaseRes.builder()
                .otherCrops(new ArrayList<>())
                .retailPrice(new ArrayList<>())
                .build();

        return setElement(baseRes, pastUglyRes, grainId);

    }

    public CropDto.BaseRes setElement(CropDto.BaseRes baseRes, CropDto.pastUglyRes pastUglyRes, Integer grainId) {

        for (CropDto.pastUgly ugly : pastUglyRes.getData()) {
            baseRes.getRetailPrice().add(ugly.getV5());
        }

        Iterable<TbCrop> cropsSameCategory = cropRepository.findByCategory(cropRepository.findByGrainId(grainId).getCategory());
        ArrayList<Integer> grainIds = new ArrayList<>();
        for (TbCrop crop : cropsSameCategory) {
            grainIds.add(crop.getCropId());
        }

        // 밑의 내용은 함수 하나 떼어내서 @Transactional로 묶어야 할 것 같음
        grainV5Fetcher.fetchAllV5(grainIds)
                .doOnNext(map -> {
                    map.forEach((grant_id, v5) ->
                            cropRepository.findByGrainId(grant_id).setCropCost(v5));
                })
                .block();

        for (Integer grainIdItem : grainIds) {
            TbCrop crop = cropRepository.findByGrainId(grainIdItem);
            baseRes.getOtherCrops().add(CropDto.OtherCrop.builder()
                    .cropEngName(crop.getCropEngName())
                    .cropKorName(crop.getCropKorName())
                    .cropCost(crop.getCropCost())
                    .build());
        }

        return baseRes;
    }

    public CropDto.pastUglyRes getPastUgly(Integer grainId) {
        return restTemplate.getForEntity("http://localhost:8000/api/past_ugly/" + grainId, CropDto.pastUglyRes.class).getBody();
    }

}
