package com.assessmint.be.ai;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;

public class GeminiService {
    @Value("${app.gemini.api-key}")
    private static String apiKey;

    @Value("${app.gemini.model}")
    private static String model;

    private static Client client = Client
            .builder()
            .apiKey(apiKey)
            .build();
}
