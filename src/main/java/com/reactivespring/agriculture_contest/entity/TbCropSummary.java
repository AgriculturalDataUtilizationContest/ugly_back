package com.reactivespring.agriculture_contest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbCropSummary extends AuditingFields {

    public Integer cropId;

    @Column(columnDefinition = "TEXT")
    private String summary;

}
