package com.abcm.jwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.jwt.entity.AppLanguage;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.service.UserService;

@RestController
@RequestMapping("/api/user/language")
public class UserLanguageController {

    @Autowired
    private UserService userService; // Inject your UserService or User repository

    @PostMapping("/set")
    public ResponseEntity<Map<String, Object>> setAppLanguageForUser(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            User user = userService.getUserFromToken(jwt);
            Long appLanguageId = Long.parseLong(String.valueOf(requestData.get("appLanguageId")));

            userService.setAppLanguageForUser(user.getId(), appLanguageId);

            response.put("status", true);
            response.put("message", "App language set successfully");
            response.put("userlanguage", user.getAppLanguage());
        } catch (Exception e) {
            status = HttpStatus.OK;
            response.put("status", false);
            response.put("message", "Error setting app language");
        }

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUserLanguage(@RequestHeader("Authorization") String jwt) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            User user = userService.getUserFromToken(jwt);
            AppLanguage appLanguage = userService.getUserAppLanguage(user.getId());

            if (appLanguage != null) {
                response.put("status", true);
                response.put("message", "User language retrieved successfully");
                response.put("language", appLanguage);
            } else {
                status = HttpStatus.NOT_FOUND;
                response.put("status", false);
                response.put("message", "User language not found");
            }
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response.put("status", false);
            response.put("message", "Error retrieving user language");
        }

        return new ResponseEntity<>(response, status);
    }

}
