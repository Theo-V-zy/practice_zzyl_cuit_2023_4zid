package com.soft.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FamilyTokenServiceTest {

    private FamilyTokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new FamilyTokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "zzyl-test-secret");
    }

    @Test
    void createsAndParsesValidFamilyToken() {
        String token = tokenService.createToken(1001);

        assertEquals(1001, tokenService.parseFamilyId(token));
    }

    @Test
    void rejectsTamperedToken() {
        String token = tokenService.createToken(1001);
        String tampered = token.substring(0, token.length() - 1) + "x";

        assertNull(tokenService.parseFamilyId(tampered));
    }

    @Test
    void rejectsMissingAndMalformedToken() {
        assertNull(tokenService.parseFamilyId(null));
        assertNull(tokenService.parseFamilyId("not-a-token"));
    }
}
