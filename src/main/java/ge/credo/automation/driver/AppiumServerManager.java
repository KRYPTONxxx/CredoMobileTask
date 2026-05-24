package ge.credo.automation.driver;

import ge.credo.automation.config.AppConfig;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AppiumServerManager {

    private static final Logger LOG = LogManager.getLogger(AppiumServerManager.class);

    private static AppiumDriverLocalService service;

    private AppiumServerManager() {
    }

    public static synchronized void start() {
        if (service != null && service.isRunning()) {
            LOG.info("Appium server already running at {}", service.getUrl());
            return;
        }

        AppConfig config = AppConfig.get();
        PortUtil.freePort(config.appiumPort());

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(config.appiumHost())
                .usingPort(config.appiumPort())
                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug");

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        LOG.info("Appium server started at {}", service.getUrl());
    }

    public static synchronized void stop() {
        if (service != null && service.isRunning()) {
            service.stop();
            LOG.info("Appium server stopped");
        }
        service = null;
    }
}
