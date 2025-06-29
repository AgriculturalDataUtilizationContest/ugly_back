package com.reactivespring.agriculture_contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reactivespring.agriculture_contest.entity.TbCrop;

public interface CropRepository extends JpaRepository<TbCrop, String> {
    Iterable<TbCrop> findByCategory(String category);

    TbCrop findByCropEngName(String cropName);

    TbCrop findByCropId(Integer grainId);
}
