package ge.credo.automation.hooks;

import ge.credo.automation.driver.AppiumServerManager;
import ge.credo.automation.driver.DriverFactory;
import ge.credo.automation.page.LoginPage;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;

public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    private final LoginPage loginPage;

    public Hooks(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @BeforeAll
    public static void startSuite() {
        AppiumServerManager.start();
        DriverFactory.start();
    }

    @AfterAll
    public static void stopSuite() {
        DriverFactory.quit();
        AppiumServerManager.stop();
    }

    @Before
    public void resetLoginFields(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
        try {
            loginPage.clearUserName().clearPassword();
        } catch (TimeoutException ignored) {
        }
    }
}
