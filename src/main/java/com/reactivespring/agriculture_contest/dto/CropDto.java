package com.reactivespring.agriculture_contest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    public static class ForecastCropInfo{
        public String cropsName;
        public String cropsImage;
    }

}
