package com.abcm.jwt.DTO;

import java.time.LocalDateTime;

public class PronounciationHistoryResponseDTO {
   // private Long userId;
    private Long id;
    private String inputPronounce;
    private String outputPronounce;
    private LocalDateTime timestamp;
    private boolean isFavorite;
    private AccentDTO accent;
    
    
    public static class AccentDTO {
        private Long id;
        private String countryName;
        private String accentName;
        private String languageName;
        private String flag;
        
        
        
        

        // Getters and Setters
        
        
        public Long getId() {
            return id;
        }

       
		public String getLanguageName() {
			return languageName;
		}

		public void setLanguageName(String languageName) {
			this.languageName = languageName;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public void setId(Long id) {
            this.id = id;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getAccentName() {
            return accentName;
        }

        public void setAccentName(String accentName) {
            this.accentName = accentName;
        }
    }
    
    public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

    
    
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AccentDTO getAccent() {
		return accent;
	}
	public void setAccent(AccentDTO accent) {
		this.accent = accent;
	}
	public String getInputPronounce() {
		return inputPronounce;
	}
	public void setInputPronounce(String inputPronounce) {
		this.inputPronounce = inputPronounce;
	}
	public String getOutputPronounce() {
		return outputPronounce;
	}
	public void setOutputPronounce(String outputPronounce) {
		this.outputPronounce = outputPronounce;
	}

    
    
    
    
    // Getters and setters
}


