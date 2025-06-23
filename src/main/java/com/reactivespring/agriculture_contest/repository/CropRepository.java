package com.reactivespring.agriculture_contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reactivespring.agriculture_contest.entity.TbCrop;

import java.util.ArrayList;

public interface CropRepository extends JpaRepository<TbCrop, String> {
    Iterable<TbCrop> findByCategory(String category);

    TbCrop findByCropName(String cropName);

    TbCrop findByGrainId(Integer grainId);
}
