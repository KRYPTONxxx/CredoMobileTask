package ge.credo.automation.page;

import ge.credo.automation.driver.DriverFactory;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected final WebDriverWait wait;

    protected BasePage() {
        this.wait = DriverFactory.getWait();
    }

    protected WebElement findByResourceId(String resourceId) {
        String escaped = resourceId.replace("\\", "\\\\").replace("\"", "\\\"");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"" + escaped + "\")")));
    }

    protected WebElement findByText(String text) {
        String escaped = text.replace("\\", "\\\\").replace("\"", "\\\"");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"" + escaped + "\")")));
    }
}
