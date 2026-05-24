package ge.credo.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final Logger LOG = LogManager.getLogger(AppConfig.class);
    private static final String RESOURCE = "config.properties";

    private static final AppConfig INSTANCE = new AppConfig();

    private final Properties properties = new Properties();

    private AppConfig() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException(RESOURCE + " not found on classpath");
            }
            properties.load(input);
            LOG.info("Loaded {} ({} keys)", RESOURCE, properties.size());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + RESOURCE, e);
        }
    }

    public static AppConfig get() {
        return INSTANCE;
    }

    public String appiumHost() {
        return require("appium.server.host");
    }

    public int appiumPort() {
        return Integer.parseInt(require("appium.server.port"));
    }

    public String appPackage() {
        return require("app.package");
    }

    public String appActivity() {
        return require("app.activity");
    }

    public String platformName() {
        return require("platform.name");
    }

    public String automationName() {
        return require("automation.name");
    }

    public String deviceName() {
        return require("device.name");
    }

    public boolean noReset() {
        return Boolean.parseBoolean(require("no.reset"));
    }

    public int defaultWaitSeconds() {
        return Integer.parseInt(require("default.wait.seconds"));
    }

    public String appiumServerUrl() {
        return "http://" + appiumHost() + ":" + appiumPort();
    }

    private String require(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration key: " + key);
        }
        return value;
    }
}
