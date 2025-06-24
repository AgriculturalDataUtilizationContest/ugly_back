package com.reactivespring.agriculture_contest.repository;

import com.reactivespring.agriculture_contest.entity.TbCropSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropSummaryRepository extends JpaRepository<TbCropSummary, String> {
    TbCropSummary findByCropId(Integer cropId);
}
