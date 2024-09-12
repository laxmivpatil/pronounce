package com.abcm.jwt.service;


 


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import java.util.Map;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource; 
import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service; 
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine; 
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;


@Service
public class EmailService {
	  private static final String DIGITS = "0123456789";
	    private static final SecureRandom random = new SecureRandom();
	    private Session session;

	   @Value("${spring.mail.host}")
	    private String host;

	    @Value("${spring.mail.port}")
	    private String port;

	    @Value("${spring.mail.username}")
	    private String senderEmail;

	    @Value("${spring.mail.password}")
	    private String senderPassword;

    
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String generateEmailContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
    
    
    
  

    /**
     * Sends an email asynchronously using JavaMailSender.
     * @param recipientEmail the recipient's email address
     * @param emailSubject the subject of the email
     * @param emailBody the body of the email (HTML content)
     * @return CompletableFuture indicating success (true) or failure (false)
     */
    @Async
    public CompletableFuture<Boolean> sendEmail(String recipientEmail, String emailSubject, String emailBody) {
        try {
            // Create a MimeMessage
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set the email details
            helper.setFrom("info@pragyagirlsschool.com");  // Sender's email
            helper.setTo(recipientEmail);              // Recipient's email
            helper.setSubject(emailSubject);           // Subject
            helper.setText(emailBody, true);           // HTML email body

            // Send the email
            emailSender.send(message);

            return CompletableFuture.completedFuture(true);  // Successfully sent
        } catch (MessagingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);  // Failure
        }
    }
 
    
    
   
    
     
    

	 
	 

	 
}

 