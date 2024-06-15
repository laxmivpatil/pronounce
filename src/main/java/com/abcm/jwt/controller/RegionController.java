package com.abcm.jwt.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.Region;
import com.abcm.jwt.service.RegionService;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping("/add")
    public ResponseEntity<String> addRegionWithLanguages(@RequestPart("regionName") String regionName,
            @RequestPart("logo") MultipartFile logo,
            @RequestPart("accents") String accents ) {
        try {
        	List<String> accentList = Arrays.asList(accents.split(","));
            regionService.addRegionWithLanguages(regionName,logo,accentList);
            return ResponseEntity.ok("Region added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add region: " + e.getMessage());
        }
    }
}
