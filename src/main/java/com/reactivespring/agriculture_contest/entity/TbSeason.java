package com.reactivespring.agriculture_contest.entity;

import jakarta.persistence.Entity;

@Entity
public class TbSeason extends AuditingFields{

    private Integer cropId;
    private String season;

}
