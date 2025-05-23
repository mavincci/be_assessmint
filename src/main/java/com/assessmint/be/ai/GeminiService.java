package com.assessmint.be.ai;

import com.google.genai.Client;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class GeminiService {
    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.model}")
    private String model;

    public Client client;

    public Client getClient() {
        if (client == null) {
            client = Client
                    .builder()
                    .apiKey(apiKey)
                    .build();
        }
        return client;
    }
}
