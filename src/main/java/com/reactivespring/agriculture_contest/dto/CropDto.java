package com.reactivespring.agriculture_contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CropDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "Forecast 창에 들어올 때의 Req DTO")
    public static class ForecastReq {
        public String category;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "Forecast 창에 들어올 때의 Req DTO")
    public static class BaseReq{
        public String cropName;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    @Schema(description = "Forecast 창에 들어올 때의 Res DTO")
    public static class ForecastResDto {
        public ArrayList<ForecastCropInfo> crops;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Schema(description = "Base부분에 들어올 Req DTO")
    public static class ForecastCropInfo{
        public String cropsName;
        public String cropsImage;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    @Schema(description = "Base 들어올 때의 Res DTO")
    public static class BaseRes {
        ArrayList<Double> retailPrice;
        ArrayList<OtherCrop> otherCrops;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    @Schema(description = "Base 들어올 때의 Res DTO의 내부 속한 것들")
    public static class OtherCrop {
        public String cropEngName;
        public String cropKorName;
        public Double cropCost;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Schema(description = "FAST API에게서 grain 과거 가격 정보 받아오는 Res DTO")
    public static class PastUglyRes {
        @JsonProperty("data")
        public ArrayList<PastUgly> data;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Schema(description = "FAST API에게서 grain 과거 가격 정보 받아오는 Res DTO의 내부 속한 것들")
    public static class PastUgly {
        public LocalDate dt;
        @JsonProperty("v_4") private double v4;
        @JsonProperty("v_5") private double v5;
        @JsonProperty("decline_ratio") private double declineRatio;
        @JsonProperty("ugly_cost") private double uglyCost;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "V5를 받아오는 Res DTO")
    public static class V5Response {
        @JsonProperty("v_5")
        private Double v5;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    public static class predictionReq {
        public String cropName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    @Schema(description = "과거 가격 데이터 가져올 때, FE에게 반환해줄 Req DTO")
    public static class predictionRes {
        List<retailPrice> retailPrice;
        List<uglyPrice> uglyPrice;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    @Schema(description = "과거 가격 데이터 가져올 때, FE에게 반환해줄 Res DTO에 속해 있는 것")
    public static class retailPrice {
        public String date;
        public Double price;
    }

    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "과거 가격 데이터 가져올 때, FE에게 반환해줄 Res DTO에 속해 있는 것")
    public static class uglyPrice {
        public String date;
        public Double price;
    }


    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "FAST API에게 미래 가격 예측 받을 때 Res에 속해있는 DTO")
    public static class futurePredictionRes {
        private LocalDate date;

        @JsonProperty("pred")
        private Double pred;

        @JsonProperty("pred_ugly")
        private Double uglyCost;
    }


    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "마켓 플레이스에서 판매하는 것을 가져와서 추천해주는 부분의 Res DTO")
    public static class recommendationRes {
        public List<marketPlaceRes> marketPlaceResList; // List<marketPlaceRes>
    }

    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    @Builder
    @Schema(description = "마켓 플레이스 Res DTO의 내부 속한 것들")
    public static class marketPlaceRes {
        public String marketImage;
        public String marketName;
        public String marketReview;
        public String marketUrl;
        public String marketWxplaination;
    }

}
