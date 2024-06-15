package com.abcm.jwt.service;

 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.AppLanguage;
import com.abcm.jwt.repository.AppLanguageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppLanguageService {
	

    @Autowired
    private StorageService storageService;  
    @Autowired
    private AppLanguageRepository languageRepository;

    public List<AppLanguage> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Optional<AppLanguage> getLanguageById(Long id) {
        return languageRepository.findById(id);
    }
    public AppLanguage saveLanguage1(  AppLanguage appL) {
    	 
        return languageRepository.save(appL);
    }
    
    
    public AppLanguage saveLanguage( String language,MultipartFile logo) {
    	String path="";
    	if (logo != null && !logo.isEmpty()) {
            path=storageService.uploadFileOnAzure(logo);
         	//String path="https://satyaprofilestorage.blob.core.windows.net/pronouncestorage/1718437967912_430d1b68-63f2-40af-84ac-f63e7bed4809.jpg?sv=2021-04-10&st=2024-06-15T07%3A47%3A48Z&se=2099-01-01T00%3A00%3A00Z&sr=b&sp=r&sig=Xq9jS5wJJE1hsNXRG1ZkvENm8iqPoC1WUYzxX9iOwuQ%3D";
             
         }
    	AppLanguage  appL=new AppLanguage();
    	appL.setLanguageName(language);
    	appL.setLogoUrl(path);
        return languageRepository.save(appL);
    }

    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
