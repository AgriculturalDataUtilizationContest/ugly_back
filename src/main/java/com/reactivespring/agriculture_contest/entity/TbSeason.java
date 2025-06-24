package com.reactivespring.agriculture_contest.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbSeason extends AuditingFields{

    private Integer cropId;
    private String season;

}
