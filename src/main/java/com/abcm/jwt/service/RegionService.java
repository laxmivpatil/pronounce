package com.abcm.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.Region;
import com.abcm.jwt.repository.AccentRepository;
import com.abcm.jwt.repository.RegionRepository;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private StorageService storageService;  

    @Autowired
    private AccentRepository accentRepository;

    public void addRegionWithLanguages(String regionName,MultipartFile logo, List<String> accents) {
        
    	String path="";
    	Region region=new Region();
    	region.setRegionName(regionName);
    	if (logo != null && !logo.isEmpty()) {
            path=storageService.uploadFileOnAzure(logo);
         	//String path="https://satyaprofilestorage.blob.core.windows.net/pronouncestorage/1718437967912_430d1b68-63f2-40af-84ac-f63e7bed4809.jpg?sv=2021-04-10&st=2024-06-15T07%3A47%3A48Z&se=2099-01-01T00%3A00%3A00Z&sr=b&sp=r&sig=Xq9jS5wJJE1hsNXRG1ZkvENm8iqPoC1WUYzxX9iOwuQ%3D";
             
         }
    	region.setLogo(path);
        region = regionRepository.save(region);

        // Iterate over the list of accents and save them
        for (String a : accents) {
        	Accent acc=new Accent();
            acc.setRegion(region); // Associate the accent with the region
            acc.setAccentName(a);
            accentRepository.save(acc);
        }
    }
}
