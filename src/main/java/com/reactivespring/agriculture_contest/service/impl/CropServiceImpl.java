package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.entity.TbCrop;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import com.reactivespring.agriculture_contest.service.GrainV5Fetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.reactivespring.agriculture_contest.service.CropService;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        Integer grainId = cropRepository.findByCropKorName(baseReq.getCropName()).getCropId();

        CropDto.PastUglyRes pastUglyRes = getPastUgly(grainId);
        CropDto.BaseRes baseRes = CropDto.BaseRes.builder()
                .otherCrops(new ArrayList<>())
                .retailPrice(new ArrayList<>())
                .build();

        return setElement(baseRes, pastUglyRes, grainId);

    }

    private CropDto.BaseRes setElement(CropDto.BaseRes baseRes, CropDto.PastUglyRes pastUglyRes, Integer grainId) {

        baseRes = addGrantIds(baseRes, pastUglyRes);

        ArrayList<Integer> grainIds = getGrainIds(grainId, pastUglyRes, baseRes);

        setGrainCostByV5(baseRes, grainIds);

        return setBaseResOthersInfo(baseRes, grainIds);
    }

    private CropDto.BaseRes setBaseResOthersInfo(CropDto.BaseRes baseRes, ArrayList<Integer> grainIds) {
        for (Integer grainIdItem : grainIds) {
            TbCrop crop = cropRepository.findByCropId(grainIdItem);
            baseRes.getOtherCrops()
                    .add(
                            CropDto.OtherCrop.builder()
                                    .cropEngName(crop.getCropEngName())
                                    .cropKorName(crop.getCropKorName())
                                    .cropCost(crop.getCropCost())
                                    .build()
                    );
        }

        return baseRes;
    }

    public void setGrainCostByV5(CropDto.BaseRes baseRes, ArrayList<Integer> grainIds) {
        grainV5Fetcher.fetchAllV5(grainIds)
                .doOnNext(map -> {
                    map.forEach((grainId, v5) -> {
                        TbCrop crop = cropRepository.findByCropId(grainId);
                        crop.setCropCost(v5);
                        cropRepository.save(crop);
                    });
                })
                .block();
    }

    private ArrayList<Integer> getGrainIds(Integer grainId, CropDto.PastUglyRes pastUglyRes, CropDto.BaseRes baseRes) {

        Iterable<TbCrop> cropsSameCategory = cropRepository.findByCategory(cropRepository.findByCropId(grainId).getCategory());

        ArrayList<Integer> grainIds = new ArrayList<>();
        for (TbCrop crop : cropsSameCategory) {
            grainIds.add(crop.getCropId());
        }

        return grainIds;
    }

    private CropDto.BaseRes addGrantIds(CropDto.BaseRes baseRes, CropDto.PastUglyRes pastUglyRes) {
        for (CropDto.PastUgly ugly : pastUglyRes.getData()) {
            baseRes.getRetailPrice().add(ugly.getV5());
        }
        return baseRes;
    }

    @Override
    public CropDto.PastUglyRes getPastUgly(Integer grainId) {
        return restTemplate.getForEntity("http://localhost:8000/api/past_ugly/" + grainId, CropDto.PastUglyRes.class).getBody();
    }

    @Override
    public CropDto.predictionRes predictionPast(CropDto.predictionReq pastUglyReq) {
        Integer cropId = getCropIdByName(pastUglyReq.getCropName());
        List<CropDto.PastUgly> pastData = getPastUgly(cropId).getData();

        List<CropDto.retailPrice> retailPrices = extractRetailPrices(pastData, 4);
        List<CropDto.uglyPrice> uglyPrices = extractUglyPrices(pastData, 4);

        return CropDto.predictionRes.builder()
                .retailPrice(retailPrices)
                .uglyPrice(uglyPrices)
                .build();
    }

    @Override
    public CropDto.predictionRes predictionFuture(CropDto.predictionReq pastUglyReq) {
        Integer cropId = getCropIdByName(pastUglyReq.getCropName());
        List<CropDto.futurePredictionRes> predictions = fetchFuturePredictions(cropId);

        List<CropDto.retailPrice> retailPrices = mapToRetailPrices(predictions);
        List<CropDto.uglyPrice> uglyPrices = mapToUglyPrices(predictions);

        return buildPredictionRes(retailPrices, uglyPrices);
    }

    private List<CropDto.futurePredictionRes> fetchFuturePredictions(Integer cropId) {
        ResponseEntity<List<CropDto.futurePredictionRes>> response = restTemplate.exchange(
                "http://localhost:8000/api/future_calc/" + cropId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return Optional.ofNullable(response.getBody()).orElse(Collections.emptyList());
    }

    private List<CropDto.retailPrice> mapToRetailPrices(List<CropDto.futurePredictionRes> predictions) {
        return predictions.stream()
                .skip(1)
                .map(p -> CropDto.retailPrice.builder()
                        .price(p.getPred())
                        .date(formatToMonthDay(p.getDate()))
                        .build())
                .toList();
    }

    private List<CropDto.uglyPrice> mapToUglyPrices(List<CropDto.futurePredictionRes> predictions) {
        return predictions.stream()
                .skip(1)
                .map(p -> CropDto.uglyPrice.builder()
                        .price(p.getUglyCost())
                        .date(formatToMonthDay(p.getDate()))
                        .build())
                .toList();
    }

    private CropDto.predictionRes buildPredictionRes(List<CropDto.retailPrice> retailPrices,
                                                     List<CropDto.uglyPrice> uglyPrices) {
        return CropDto.predictionRes.builder()
                .retailPrice(retailPrices)
                .uglyPrice(uglyPrices)
                .build();
    }


    private Integer getCropIdByName(String cropName) {
        return cropRepository.findByCropKorName(cropName).getCropId();
    }

    private List<CropDto.retailPrice> extractRetailPrices(List<CropDto.PastUgly> data, int limit) {
        return data.stream()
                .limit(limit)
                .map(p -> CropDto.retailPrice.builder()
                        .price(p.getV5())
                        .date(formatToMonthDay(p.getDt()))
                        .build())
                .toList();
    }

    private List<CropDto.uglyPrice> extractUglyPrices(List<CropDto.PastUgly> data, int limit) {
        return data.stream()
                .limit(limit)
                .map(p -> CropDto.uglyPrice.builder()
                        .price(p.getUglyCost())
                        .date(formatToMonthDay(p.getDt()))
                        .build())
                .toList();
    }

    private String formatToMonthDay(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd"));
    }


}
