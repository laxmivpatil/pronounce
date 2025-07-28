package com.abcm.jwt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WordController {

    private static final List<String> WORDS = List.of(
            "sunshine", "breeze", "harmony", "echo", "twilight", "serenity", "vibe", "glow", "pulse", "mellow",
            "storm", "mist", "drizzle", "spark", "chime", "rhythm", "hum", "shade", "zen", "aura"
    );

    private final Random random = new Random();

   
    @GetMapping("/random-words")
    public ResponseEntity<Map<String, Object>> getRandomWords() {
        Set<String> randomWords = new LinkedHashSet<>();
        while (randomWords.size() < 10) {
            int index = random.nextInt(WORDS.size());
            randomWords.add(WORDS.get(index));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("words", new ArrayList<>(randomWords));

        return ResponseEntity.ok(response);
    }
}
