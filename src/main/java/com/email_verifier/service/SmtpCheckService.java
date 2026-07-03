package com.email_verifier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class SmtpCheckService {

    @Value("${abstract.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public SmtpCheckService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://emailvalidation.abstractapi.com")
                .build();
    }

    public boolean verifyEmail(String email, String mxHost) {
        try {
            String response = webClient.get()
                    .uri(uri -> uri
                            .path("/v1/")
                            .queryParam("api_key", apiKey)
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            boolean smtpValid = root
                    .path("is_smtp_valid")
                    .path("value")
                    .asBoolean();

            String deliverability = root
                    .path("deliverability")
                    .asText();

            System.out.println("📬 Abstract API: " + deliverability);

            return smtpValid ||
                    "DELIVERABLE".equals(deliverability);

        } catch (Exception e) {
            System.out.println("❌ API Error: " + e.getMessage());
            return false;
        }
    }
}