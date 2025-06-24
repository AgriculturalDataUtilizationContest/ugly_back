package com.reactivespring.agriculture_contest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class TbCropSummary extends AuditingFields {

    public Integer cropId;

    @Column(columnDefinition = "TEXT")
    private String summary;

}
