package com.abcm.jwt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
	
	@Autowired
	private EmailService emailService;

    private static final int OTP_VALIDITY = 5 * 60 * 1000;  // OTP valid for 5 minutes

    // Map to store the generated OTPs with a timestamp
    private Map<String, OtpEntry> otpStorage = new HashMap<>();

    // Method to generate OTP
    public String generateOtp(String email) {
        String otp = String.format("%04d", new Random().nextInt(10000));  // Generates a 4-digit OTP
        otpStorage.put(email, new OtpEntry(otp, System.currentTimeMillis()));
        return otp;
    }


    // Method to send OTP (e.g., via SMS)
    public void sendOtp(String otp, String email) {
        // Logic to send OTP via SMS or email
    	 Map<String, Object> variables = new HashMap<>();
         variables.put("otp", otp);
         

         // Generate email content using Thymeleaf
         String emailBody = emailService.generateEmailContent("OtpEmail", variables);
       
         //remove comment after use
         // emailService.sendEmail(email, "OTP Verification", emailBody);
         
        System.out.println("OTP " + otp + " sent to " + email);
    }

    // Method to validate OTP
    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpStorage.get(email);
        if (entry != null && entry.getOtp().equals(otp)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - entry.getTimestamp() < OTP_VALIDITY) {
                return true;
            } else {
                otpStorage.remove(email);  // OTP expired
            }
        }
        return false;
    }

    // Nested class to store OTP with timestamp
    private static class OtpEntry {
        private String otp;
        private long timestamp;

        public OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
