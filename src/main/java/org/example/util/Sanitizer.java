package org.example.util;

public final class Sanitizer {
    private Sanitizer() {}

    public static String sanitizeForLog(String s) {
        if (s == null) return "null";
        return s.replaceAll("[\\r\\n\\t]+", " ").trim();
    }
}
