package com.reactivespring.agriculture_contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

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
        ArrayList<Integer> retailPrice;
        ArrayList<OtherCrop> otherCrops;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @Setter
    public static class OtherCrop {
        public String cropEngName;
        public String cropKorName;
        public double cropCost;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class  pastUglyRes {
        @JsonProperty("data")
        public ArrayList<pastUgly> data;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class pastUgly {
        public LocalDate dt;
        @JsonProperty("v_4") private int v4;
        @JsonProperty("v_5") private int v5;
        @JsonProperty("decline_ratio") private double declineRatio;
        @JsonProperty("ugly_cost") private double uglyCost;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class V5Response {
        @JsonProperty("v_5")
        private Double v5;
    }


}
