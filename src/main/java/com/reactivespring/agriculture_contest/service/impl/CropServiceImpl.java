package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.entity.TbCrop;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import com.reactivespring.agriculture_contest.service.GrainV5Fetcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.reactivespring.agriculture_contest.service.CropService;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public CropDto.predictionPastRes predictionPast(CropDto.predictionPastReq pastUglyReq) {
        Integer cropId = getCropIdByName(pastUglyReq.getCropName());
        List<CropDto.PastUgly> pastData = getPastUgly(cropId).getData();

        List<CropDto.retailPrice> retailPrices = extractRetailPrices(pastData, 4);
        List<CropDto.uglyPrice> uglyPrices = extractUglyPrices(pastData, 4);

        return CropDto.predictionPastRes.builder()
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
        return date.format(DateTimeFormatter.ofPattern("MM-dd"));
    }


}
