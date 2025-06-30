package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.entity.TbCrop;
import com.reactivespring.agriculture_contest.exception.type.NotFoundException;
import com.reactivespring.agriculture_contest.naver.NaverClient;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import com.reactivespring.agriculture_contest.repository.CropSeasonRepository;
import com.reactivespring.agriculture_contest.repository.CropSummaryRepository;
import com.reactivespring.agriculture_contest.service.GrainV5Fetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.reactivespring.agriculture_contest.service.CropService;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.jsoup.Jsoup;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final GrainV5Fetcher grainV5Fetcher;
    private final RestTemplate restTemplate;
    private final CropSummaryRepository cropSummaryRepository;
    private final NaverClient naverClient;
    private final CropSeasonRepository cropSeasonRepository;

    @Value("${fastapi.base-url}")
    public String fastApiBaseUrl;

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

        Integer grainId = cropRepository.findByCropEngName(baseReq.getCropName()).getCropId().describeConstable().orElseThrow(() -> new NotFoundException(baseReq.getCropName() + " not found"));

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
                                    .cropsImage(crop.getCropsImage())
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
        return restTemplate.getForEntity(fastApiBaseUrl + "/pyapi/past_ugly/" + grainId, CropDto.PastUglyRes.class).getBody();
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

    @Override
    public CropDto.issueCheckRes issueCheck(CropDto.predictionReq issueCheckReq) {
        String cropIssue = getCropIssueByAI(issueCheckReq.getCropName());
        String wordCloud = getWordCloud(issueCheckReq.getCropName());
        List<String> news = getNews(issueCheckReq.getCropName());

        CropDto.issueCheckRes issueCheckRes = CropDto.issueCheckRes.builder()
                .cropIssue(cropIssue)
                .wordCloud(wordCloud)
                .news(news)
                .build();

        return issueCheckRes;
    }

    @Override
    public CropDto.recommendationRes recommendation(CropDto.predictionReq recommendationReq) {
        CropDto.recommendationRes recommendationRes = getNaverRecommendation(recommendationReq.getCropName());
        return recommendationRes;
    }

    @Override
    public CropDto.comparisonPriceRes comparePrice(CropDto.predictionReq comparisonPriceReq) {

        var data = getCropsPastPrice(comparisonPriceReq);

        // 금일 data
        var todayData = data.getData().get(0);
        return CropDto.comparisonPriceRes.builder()
                .difference((int) abs(todayData.getV5() - todayData.getUglyCost()))
                .discountRate((int) (todayData.getUglyCost() - todayData.getV4()) / 100)
                .uglyPrice((int) todayData.getUglyCost())
                .marketPrice((int) todayData.getV4())
                .build();
    }

    @Override
    public CropDto.comparisonCategoryRes compareCategory(CropDto.ForecastReq comparisonCategoryReq) {
        CropDto.comparisonCategoryRes comparisonCategoryRes = CropDto.comparisonCategoryRes.builder()
                .cropList(new ArrayList<>())
                .build();

        cropRepository.findByCategory(comparisonCategoryReq.getCategory())
                .forEach(crop -> {
                    var pastPriceData = getPastUgly(crop.getCropId()).getData();
                    CropDto.comparisonCategory comparisonCategory = CropDto.comparisonCategory.builder()
                            .cropName(crop.getCropKorName())
                            .cropCategory(crop.getCategory())
                            .cropPrice((int) pastPriceData.get(0).getUglyCost())
                            .increaseRate(100 * (pastPriceData.get(1).getUglyCost() / pastPriceData.get(0).getUglyCost()))
                            .cropsImage(crop.getCropsImage())
                            .build();
                    comparisonCategoryRes.getCropList().add(comparisonCategory);
                });

        return comparisonCategoryRes;
    }

    @Override
    public CropDto.searchTwoRes searchTwo() {
        CropDto.searchTwoRes searchTwoRes = CropDto.searchTwoRes.builder()
                .cropList(new ArrayList<>())
                .build();

        String currentSeason = getCurrentSeason();

        cropSeasonRepository.findBySeason(currentSeason).forEach(crop -> {
                    TbCrop tbCrop = cropRepository.findByCropId(crop.getCropId());
                    var data = getPastUgly(tbCrop.getCropId()).getData();
                    if (tbCrop != null) {
                        CropDto.searchCrop searchCrop = CropDto.searchCrop.builder()
                                .cropName(tbCrop.getCropKorName())
                                .cropCategory(tbCrop.getCategory())
                                .cropsImage(tbCrop.getCropsImage())
                                .cropPrice((int) data.get(0).getUglyCost())
                                .increaseRate(100 * (data.get(1).getUglyCost() / data.get(0).getUglyCost()))
                                .build();
                        searchTwoRes.getCropList().add(searchCrop);
                    }
                });


        return searchTwoRes;
    }

//    @Override
//    public CropDto.searchOneRes searchOne() {
//        cropRepository.findAll().forEach(crop -> {
//            TbCrop tbCrop = cropRepository.findByCropId(crop.getCropId());
//            var data = getPastUgly(tbCrop.getCropId()).getData();
//            if (tbCrop != null) {
//                CropDto.searchOneCrop searchOne = CropDto.searchOneCrop.builder()
//                        .cropKorName(tbCrop.getCropKorName())
//                        .cropEngName(tbCrop.getCropEngName())
//                        .cropsImage(tbCrop.getCropsImage())
//                        .increaseRate(data.get(0).getDeclineRatio())
//                        .build();
//                return searchOne;
//            }
//        })
//
//        return null;
//    }

    private String getCurrentSeason() {
        var month = LocalDate.now().getMonth();
        if(month == Month.DECEMBER || month == Month.JANUARY || month == Month.FEBRUARY) {
            return "WINTER";
        } else if(month == Month.MARCH || month == Month.APRIL || month == Month.MAY) {
            return "SPRING";
        } else if(month == Month.JUNE || month == Month.JULY || month == Month.AUGUST) {
            return "SUMMER";
        } else {
            return "FALL";
        }
    }

    private CropDto.PastUglyRes getCropsPastPrice(CropDto.predictionReq comparisonPriceReq) {
        return restTemplate.getForEntity(
                fastApiBaseUrl + "/pyapi/past_ugly/{grain_id}",
                CropDto.PastUglyRes.class, cropRepository.findByCropEngName(comparisonPriceReq.getCropName()).getCropId()
        ).getBody();
    }

    private CropDto.recommendationRes getNaverRecommendation(String cropName) {
        var resp = naverClient.searchShop(cropName);

        List<CropDto.marketPlaceRes> markets = resp.getItems().stream()
                .map(item -> CropDto.marketPlaceRes.builder()
                        .marketName(Jsoup.parse(item.getTitle()).text() )
                        .marketUrl(item.getLink())
                        .marketImage(item.getImage())
                        .marketReview(item.getMallName())
                        .marketExplaination( String.format("최저가 %,d원", item.getLprice()) )
                        .build())
                .toList();

        return CropDto.recommendationRes.builder()
                .markets(markets)
                .build();
    }

    private List<String> getNews(String cropName) {
        List<String> newsList = new ArrayList<>();

        NaverClient.NaverSearchResponse resp = naverClient.searchNews(cropName + " \"농작물\"", 10, 1, "date");

        newsList = resp.getItems().stream()
                .map(NaverClient.NaverSearchResponse.Item::getOriginallink)
                .filter(Objects::nonNull)     // null 보호
                .collect(Collectors.toList());

        return newsList;
    }

    private String getWordCloud(String cropName) {
        return restTemplate.getForEntity(fastApiBaseUrl + "/pyapi/generate?cropName=" + cropName, String.class).getBody();
    }

    //  issue는 매주 월 9시에 실행되어서 DB에 저장됨.
    private String getCropIssueByAI(String cropName) {
        return cropSummaryRepository.findByCropId(cropRepository.findByCropEngName(cropName).getCropId()).getSummary().describeConstable().orElseThrow(() -> new NotFoundException(cropName + "에 대한 이슈가 아직 등록되지 않았습니다. (chat GPT 재확인)"));
    }

    private List<CropDto.futurePredictionRes> fetchFuturePredictions(Integer cropId) {
        ResponseEntity<List<CropDto.futurePredictionRes>> response = restTemplate.exchange(
                fastApiBaseUrl + "/pyapi/future_calc/" + cropId,
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
        return cropRepository.findByCropEngName(cropName).getCropId().describeConstable().orElseThrow(() -> new NotFoundException(cropName + "은 없는 작물입니다."));
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
