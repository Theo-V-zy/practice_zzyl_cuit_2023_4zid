package com.soft.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class FamilyTokenService {

    private static final long TOKEN_TTL_SECONDS = 7 * 24 * 60 * 60;

    @Value("${family.token.secret:zzyl-family-dev-secret-change-me}")
    private String secret;

    public String createToken(Integer familyId) {
        long expiresAt = Instant.now().getEpochSecond() + TOKEN_TTL_SECONDS;
        String payload = familyId + ":" + expiresAt;
        return encode(payload) + "." + sign(payload);
    }

    public Integer parseFamilyId(String token) {
        if (token == null || token.isBlank() || !token.contains(".")) {
            return null;
        }
        try {
            String[] parts = token.split("\\.", 2);
            String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            if (!constantTimeEquals(sign(payload), parts[1])) {
                return null;
            }
            String[] values = payload.split(":", 2);
            if (Long.parseLong(values[1]) < Instant.now().getEpochSecond()) {
                return null;
            }
            return Integer.valueOf(values[0]);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot create family token", exception);
        }
    }

    private boolean constantTimeEquals(String left, String right) {
        if (left.length() != right.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < left.length(); i++) {
            result |= left.charAt(i) ^ right.charAt(i);
        }
        return result == 0;
    }
}
