package ge.credo.automation.config;

import java.util.Arrays;
import java.util.Locale;

public enum SupportedLocale {

    KA("ka", "ქართული"),
    EN("en", "English"),
    RU("ru", "Русский");

    private final String code;
    private final String displayLabel;

    SupportedLocale(String code, String displayLabel) {
        this.code = code;
        this.displayLabel = displayLabel;
    }

    public String code() {
        return code;
    }

    public String displayLabel() {
        return displayLabel;
    }

    public Locale toJavaLocale() {
        return Locale.forLanguageTag(code);
    }

    public static SupportedLocale from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Locale value must not be blank");
        }
        String trimmed = value.trim();
        return Arrays.stream(values())
                .filter(l -> l.code.equalsIgnoreCase(trimmed) || l.displayLabel.equals(trimmed))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported locale: " + value));
    }
}
