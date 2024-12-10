package com.abcm.jwt.repository;

 
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abcm.jwt.entity.PronounciationHistory;

public interface PronounciationHistoryRepository extends JpaRepository<PronounciationHistory, Long> {
    // Custom query methods if needed
	
	 // Fetch all history for a specific user
    List<PronounciationHistory> findByUserId(Long userId);

    // Fetch all history for a specific accent
    List<PronounciationHistory> findByAccentId(Long accentId);

    // Fetch history for a specific user and accent
    @Query("SELECT p FROM PronounciationHistory p WHERE p.user.id = :userId AND p.accent.id = :accentId")
    List<PronounciationHistory> findByUserIdAndAccentId(@Param("userId") Long userId, @Param("accentId") Long accentId);
    
    List<PronounciationHistory> findByUserIdAndFavoriteTrue(Long userId);

    Optional<PronounciationHistory> findByIdAndUserId(Long id, Long userId);

}
