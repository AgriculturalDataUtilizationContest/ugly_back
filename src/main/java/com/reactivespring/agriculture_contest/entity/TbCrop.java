package com.reactivespring.agriculture_contest.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class TbCrop extends AuditingFields {

    // 생성과 동시에 들어가 있어야 하는 것
    private String cropId;  // kamis에서 정보 찾아올 때 쓰는 그 코드
    private String cropKorName;
    private String cropEngName;
    private String category;
    private String cropsImage;
    private String cropModelId; // 가격 예측에 사용하는

    // 차후에 넣을 것
    private String cropCost;
    private String cropDescription;

}
