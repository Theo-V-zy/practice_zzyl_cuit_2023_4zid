package com.soft.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class AIChatController {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    private static final String SYSTEM_PROMPT =
        "你是中州养老院的AI助手，专门为养老院工作人员和家属提供帮助。" +
        "你的职责包括：解答养老护理相关问题、介绍养老院服务项目、" +
        "帮助理解护理计划、解答老人健康管理疑问等。" +
        "请用中文回答，语气专业、温和、有耐心。回答尽量简洁。";

    @PostMapping("/stream")
    public SseEmitter chatStream(@RequestBody Map<String, Object> body) {
        String userMessage = (String) body.get("message");
        // 获取历史消息（可选）
        @SuppressWarnings("unchecked")
        List<Map<String, String>> history = (List<Map<String, String>>) body.get("history");

        SseEmitter emitter = new SseEmitter(120000L);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // 构建请求体
                StringBuilder jsonBody = new StringBuilder();
                jsonBody.append("{\"model\":\"").append(model).append("\",");
                jsonBody.append("\"stream\":true,");
                jsonBody.append("\"messages\":[");
                // system prompt
                jsonBody.append("{\"role\":\"system\",\"content\":\"").append(escapeJson(SYSTEM_PROMPT)).append("\"}");
                // 历史消息
                if (history != null) {
                    for (Map<String, String> msg : history) {
                        jsonBody.append(",{\"role\":\"").append(msg.get("role")).append("\",");
                        jsonBody.append("\"content\":\"").append(escapeJson(msg.get("content"))).append("\"}");
                    }
                }
                // 当前用户消息
                jsonBody.append(",{\"role\":\"user\",\"content\":\"").append(escapeJson(userMessage)).append("\"}");
                jsonBody.append("]}");

                // 连接 DeepSeek API
                URI uri = new URI(apiUrl + "/chat/completions");
                conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(120000);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
                }

                // 读取 SSE 流
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder fullText = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String data = line.substring(6);
                            if ("[DONE]".equals(data)) {
                                break;
                            }
                            try {
                                // 解析 JSON 提取 content
                                String content = extractContent(data);
                                if (content != null && !content.isEmpty()) {
                                    fullText.append(content);
                                    emitter.send(SseEmitter.event().data(content));
                                }
                            } catch (Exception ignored) {
                                // 跳过解析失败的行
                            }
                        }
                    }
                }
                emitter.complete();
            } catch (Exception e) {
                try {
                    emitter.send(SseEmitter.event().data("[错误] " + e.getMessage()));
                } catch (Exception ignored) {}
                try { emitter.completeWithError(e); } catch (Exception ignored) {}
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();

        return emitter;
    }

    /**
     * 从 DeepSeek SSE 返回的 JSON 中提取 content 字段
     * 格式: {"choices":[{"delta":{"content":"xxx"}}]}
     */
    private String extractContent(String json) {
        // 简单提取，避免引入完整 JSON 解析库
        int idx = json.indexOf("\"content\":\"");
        if (idx < 0) return null;
        idx += 11;
        StringBuilder sb = new StringBuilder();
        boolean escaping = false;
        for (int i = idx; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaping) {
                switch (c) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    default: sb.append(c); break;
                }
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
