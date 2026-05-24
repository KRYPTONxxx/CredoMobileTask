package ge.credo.automation.i18n;

import ge.credo.automation.config.SupportedLocale;

import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public final class Messages {

    private static final String BUNDLE_BASE = "i18n.messages";

    private Messages() {
    }

    public static String get(SupportedLocale locale, String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                BUNDLE_BASE, locale.toJavaLocale(), new Utf8Control());
        return bundle.getString(key);
    }

    private static final class Utf8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, java.util.Locale locale, String format,
                                        ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, java.io.IOException {
            if (!"java.properties".equals(format)) {
                return super.newBundle(baseName, locale, format, loader, reload);
            }
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (java.io.InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream == null) {
                    return null;
                }
                try (java.io.InputStreamReader reader =
                             new java.io.InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    return new java.util.PropertyResourceBundle(reader);
                }
            }
        }
    }
}
