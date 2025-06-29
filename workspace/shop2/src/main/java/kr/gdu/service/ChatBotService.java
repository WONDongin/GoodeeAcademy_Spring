package kr.gdu.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatBotService {
	public String getChatGPTResponse(String question) 
    		throws URISyntaxException, IOException, InterruptedException{
	    final String API_KEY = "OPEN AI API_KEY";
	    final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
        HttpClient client = HttpClient.newHttpClient();
        //requestBody : chatgpt에 요청 body
        Map<String, Object> requestBody = new HashMap<>();
        // API에서 제공되는 모델 선택. 모델별 비용 차이있음
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[] {
//            new HashMap<String, String>() {{
//                put("role", "system"); //AI의 전문영역 지정
//                put("content", "당신은 파이썬 전문가 입니다.");
//            }},
            new HashMap<String, String>() {{ //질문 내용
                put("role", "user");
                put("content", question);
            }}
        });
        // JSON 변환
        ObjectMapper objectMapper = new ObjectMapper();
        //Map => Json 형식의 문자열로 변환
        String requestBodyJson=objectMapper.writeValueAsString(requestBody);
        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();
        // 요청 보내고 응답 받기
        HttpResponse<String> response = //chat Gpt의 응답 객체
        		client.send(request, HttpResponse.BodyHandlers.ofString());
        // 응답 처리
        if (response.statusCode() == 200) {
        	Map<String, Object> responseBody = objectMapper.readValue
        	 (response.body(), new TypeReference<Map<String, Object>>() {});
        	//chat gpt의 형식대로 구현
            List<Map<String, Object>> choices = (List<Map<String, Object>>) 
            		                      responseBody.get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, String> message = (Map<String, String>) 
            		                     firstChoice.get("message");
            return message.get("content"); //chat gpt의 응답 데이터
        } else {
            throw new RuntimeException("API 요청 실패: " + response.body());
        }
	}
}
