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
    private Integer cropId;  // kamis에서 정보 찾아올 때 쓰는 그 코드
    private String cropKorName;
    private String cropEngName;
    private String category;
    private String cropsImage;

    // 차후에 넣을 것
    @Setter private Double cropCost;
    @Setter private Double uglyCost;

}
