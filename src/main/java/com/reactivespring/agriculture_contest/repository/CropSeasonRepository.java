package com.reactivespring.agriculture_contest.repository;

import com.reactivespring.agriculture_contest.entity.TbSeason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropSeasonRepository extends JpaRepository<TbSeason, Long> {
    List<TbSeason> findBySeason(String currentSeason);
}
