package com.mdd.chat.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hl
 * @version 1.0
 * @description
 * @date 2023/8/5 23:07
 */
@RestController
public class ChatController {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";
    @PostMapping("/chat")
    public String chat(@RequestParam String message, @RequestParam String systemRole){
        try {
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            // 构建请求体
            Map<String, Object> data = new HashMap<>();
            data.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();

            // 添加用户消息
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", message);
            messages.add(userMsg);

            // 添加系统消息
            Map<String, String> sysMsg = new HashMap<>();
            sysMsg.put("role", "system");
            sysMsg.put("content", systemRole);
            messages.add(sysMsg);

            data.put("messages", messages);
            data.put("temperature", 0.7);

            Gson gson = new Gson();
            String requestBody = gson.toJson(data);

            // 发送请求
            conn.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
