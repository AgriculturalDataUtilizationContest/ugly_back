package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.service.CropService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CropService cropService;

    public SearchController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping("/two")
    public ResponseEntity<CropDto.searchTwoRes> searchTwo () {
        return ResponseEntity.ok().body(cropService.searchTwo());
    }

//    @GetMapping("/one")
//    public ResponseEntity<CropDto.searchOneRes> searchOne () {
//        return ResponseEntity.ok().body(cropService.searchOne());
//    }

}
