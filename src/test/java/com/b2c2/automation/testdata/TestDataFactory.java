package com.b2c2.automation.testdata;

import java.util.Map;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Map<String, String> validContactFormData() {
        return Map.of(
            "name", "Test User",
            "email", "test@example.com",
            "company", "Test Corp",
            "position", "QA Engineer",
            "message", "This is an automated test — please ignore."
        );
    }
}
