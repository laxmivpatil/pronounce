package com.abcm.jwt.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcm.jwt.DTO.PronounciationHistoryDTO;
import com.abcm.jwt.DTO.PronounciationHistoryResponseDTO;
import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.PronounciationHistory;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.repository.AccentRepository;
import com.abcm.jwt.repository.PronounciationHistoryRepository;
import com.abcm.jwt.repository.UserRepository;

@Service
public class PronounciationHistoryService {
	@Autowired
	private PronounciationHistoryRepository pronounciationHistoryRepository;
	
	@Autowired
	private AccentRepository accentRepository;
	
	@Autowired
	private UserRepository userRepository;
	

	public List<PronounciationHistoryResponseDTO> toDtoList(List<PronounciationHistory> histories) {
        return histories.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PronounciationHistoryResponseDTO toDto(PronounciationHistory history) {
        PronounciationHistoryResponseDTO dto = new PronounciationHistoryResponseDTO();
        
        dto.setId(history.getId());
        dto.setInputPronounce(history.getInputPronounce());
        dto.setOutputPronounce(history.getOutputPronounce());
        dto.setTimestamp(history.getTimestamp());
        dto.setFavorite(history.isFavorite());

        // Map Accent
        PronounciationHistoryResponseDTO.AccentDTO accentDTO = new PronounciationHistoryResponseDTO.AccentDTO();
        accentDTO.setId(history.getAccent().getId());
        accentDTO.setCountryName(history.getAccent().getCountryName());
        accentDTO.setAccentName(history.getAccent().getAccentName());
        accentDTO.setFlag(history.getAccent().getFlag());
        accentDTO.setLanguageName(history.getAccent().getLanguage().getLanguageName());
        dto.setAccent(accentDTO);

        return dto;
    }
	
	
	public  PronounciationHistoryResponseDTO savePronounciationHistory(Long userId, Long accentId, String inputPronounce, String outputPronounce) {
	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	    Accent accent = accentRepository.findById(accentId).orElseThrow(() -> new RuntimeException("Accent not found"));

	    PronounciationHistory history = new PronounciationHistory();
	    history.setUser(user);
	    history.setAccent(accent);
	    history.setInputPronounce(inputPronounce);
	    history.setOutputPronounce(outputPronounce);
	    history.setTimestamp(LocalDateTime.now());

	    PronounciationHistory historyUpdated=pronounciationHistoryRepository.save(history);
	    return toDto(historyUpdated);
	}
	
	
	  // Fetch all history for a specific user
    public List<PronounciationHistoryResponseDTO> getHistoryByUserId(Long userId) {
    	List<PronounciationHistory> p= pronounciationHistoryRepository.findByUserId(userId);
    	return toDtoList(p);
    }

    // Fetch all history for a specific accent
    public List<PronounciationHistory> getHistoryByAccentId(Long accentId) {
        return pronounciationHistoryRepository.findByAccentId(accentId);
    }

    // Fetch history for a specific user and accent
    public List<PronounciationHistory> getHistoryByUserIdAndAccentId(Long userId, Long accentId) {
        return pronounciationHistoryRepository.findByUserIdAndAccentId(userId, accentId);
    }

    public PronounciationHistoryResponseDTO  markAsFavorite(Long historyId, Long userId) {
        PronounciationHistory history = pronounciationHistoryRepository.findByIdAndUserId(historyId, userId)
                .orElseThrow(() -> new RuntimeException("History entry not found or does not belong to the user"));

        history.setFavorite(true);
        
        PronounciationHistory history1= pronounciationHistoryRepository.save(history);
     
        return toDto(history1);
    }
    public PronounciationHistoryResponseDTO updateFavoriteStatus(Long pronounceId, Long userId, boolean status) {
        // Fetch the pronunciation history using pronounceId and userId
        PronounciationHistory history = pronounciationHistoryRepository.findByIdAndUserId(pronounceId, userId)
                .orElseThrow(() -> new RuntimeException("Pronunciation history not found"));

        // Update the status
        history.setFavorite(status);
        pronounciationHistoryRepository.save(history);

        // Convert entity to DTO and return
        return toDto(history);
    }


    public List<PronounciationHistoryResponseDTO> getFavoritesByUser(Long userId) {
    	 List<PronounciationHistory> history =pronounciationHistoryRepository.findByUserIdAndFavoriteTrue(userId);
    	 return toDtoList(history);
    }
	

}
