package ge.credo.automation.driver;

import ge.credo.automation.config.AppConfig;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);
    private static final String PERMISSION_DENY_ID = "com.android.permissioncontroller:id/permission_deny_button";
    private static final String ONBOARDING_SKIP_ID = "skipButton";
    private static final String ONBOARDING_FINISH_ID = "finishButton";

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> WAIT = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void start() {
        if (DRIVER.get() != null) {
            LOG.info("Driver already initialized on thread {}", Thread.currentThread().getName());
            return;
        }

        AppConfig config = AppConfig.get();
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.platformName())
                .setAutomationName(config.automationName())
                .setAppPackage(config.appPackage())
                .setAppActivity(config.appActivity())
                .setNoReset(config.noReset())
                .setDeviceName(config.deviceName());

        AndroidDriver driver = new AndroidDriver(serverUrl(config), options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.defaultWaitSeconds()));
        DRIVER.set(driver);
        WAIT.set(wait);
        LOG.info("AndroidDriver initialized against {}", config.appiumServerUrl());

        dismissPermissionDialog(wait);
        dismissOnboarding(wait);
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not started on thread " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static WebDriverWait getWait() {
        WebDriverWait wait = WAIT.get();
        if (wait == null) {
            throw new IllegalStateException("WebDriverWait not started on thread " + Thread.currentThread().getName());
        }
        return wait;
    }

    public static void quit() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
                LOG.info("AndroidDriver quit on thread {}", Thread.currentThread().getName());
            } catch (RuntimeException e) {
                LOG.warn("Error quitting driver: {}", e.getMessage());
            } finally {
                DRIVER.remove();
                WAIT.remove();
            }
        }
    }

    private static URL serverUrl(AppConfig config) {
        try {
            return URI.create(config.appiumServerUrl()).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL: " + config.appiumServerUrl(), e);
        }
    }

    private static void dismissPermissionDialog(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(PERMISSION_DENY_ID))).click();
            LOG.info("Dismissed system permission dialog");
        } catch (TimeoutException ignored) {
            LOG.debug("No permission dialog appeared");
        }
    }

    private static void dismissOnboarding(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.androidUIAutomator(uiSelectorById(ONBOARDING_SKIP_ID)))).click();
            LOG.info("Dismissed onboarding (skip)");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                        AppiumBy.androidUIAutomator(uiSelectorById(ONBOARDING_FINISH_ID)))).click();
                LOG.info("Dismissed onboarding (finish)");
            } catch (TimeoutException ignored) {
                LOG.debug("No onboarding finish step");
            }
        } catch (TimeoutException ignored) {
            LOG.debug("No onboarding flow");
        }
    }

    private static String uiSelectorById(String id) {
        return "new UiSelector().resourceId(\"" + id + "\")";
    }
}
