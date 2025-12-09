package com.example.demo.service;

import com.example.demo.dto.ImageRequest;
import com.example.demo.dto.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    // 프롬프트 뒤에 붙일 안전 문구
    private static final String SAFE_SUFFIX =
            " Book cover generation." +
                    "Keep it clean, professional, and appropriate for all ages." +
                    "No sexual, violent, or offensive elements.";


    private static final int MAX_PROMPT_LENGTH = 1000;

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        // HttpComponentsClientHttpRequestFactory 적용
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public ImageResponse generateImage(String prompt) {

        // 전체 길이 제한 검사 (사용자 프롬프트 + 안전문구 포함)
        if ((prompt + SAFE_SUFFIX).length() > MAX_PROMPT_LENGTH) {
            ImageResponse error = new ImageResponse();
            int totalLength = prompt.length() + SAFE_SUFFIX.length();
            error.setErrorMessage("프롬프트가 너무 깁니다. (현재 길이: " + totalLength + " / 제한: 1000)");
            return error;
        }

        String refinedPrompt = prompt + SAFE_SUFFIX;

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey.trim());

        // 요청 바디 구성
        ImageRequest requestPayload = new ImageRequest(refinedPrompt, 1, "1024x1024");
        HttpEntity<ImageRequest> entity = new HttpEntity<>(requestPayload, headers);

        // API 요청
        ResponseEntity<ImageResponse> response = restTemplate.postForEntity(
                apiUrl,
                entity,
                ImageResponse.class
        );

        ImageResponse body = response.getBody();

        // 정상 응답
        if (body != null && body.getData() != null && !body.getData().isEmpty()) {
            return body;
        }

        // 실패 응답
        ImageResponse emptyResponse = new ImageResponse();
        emptyResponse.setErrorMessage("이미지가 생성되지 않았습니다.");
        return emptyResponse;
    }
}
