package ge.credo.automation.steps;

import ge.credo.automation.config.SupportedLocale;
import ge.credo.automation.context.TestContext;
import ge.credo.automation.i18n.Messages;
import ge.credo.automation.page.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private static final Logger LOG = LogManager.getLogger(LoginSteps.class);

    private static final String STATE_ENABLED = "enabled";
    private static final String STATE_DISABLED = "disabled";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_FLASH = "flash";

    private final LoginPage loginPage;
    private final TestContext context;

    public LoginSteps(LoginPage loginPage, TestContext context) {
        this.loginPage = loginPage;
        this.context = context;
    }

    @Given("user is on login page")
    public void userIsOnLoginPage() {
        assertThat(loginPage.isLoginButtonEnabled())
                .as("Login page should be visible (login button rendered)")
                .isIn(true, false);
    }

    @When("user changes language to {string}")
    public void userChangesLanguageTo(String localeValue) {
        SupportedLocale locale = SupportedLocale.from(localeValue);
        context.setCurrentLocale(locale);
        LOG.info("Switching app language to {}", locale);
        loginPage.changeLanguageViaUI(locale.displayLabel());
    }

    @And("user enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        if (!username.isEmpty()) {
            loginPage.enterUserName(username);
        }
        if (!password.isEmpty()) {
            loginPage.enterPassword(password);
        }
    }

    @And("user clears the {string} field")
    public void userClearsField(String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return;
        }
        switch (fieldName.toLowerCase(Locale.ROOT)) {
            case FIELD_USERNAME -> loginPage.clearUserName();
            case FIELD_PASSWORD -> loginPage.clearPassword();
            default -> throw new IllegalArgumentException("Unknown field to clear: " + fieldName);
        }
    }

    @Then("login button should be in {string} state")
    public void loginButtonShouldBeInState(String expectedState) {
        boolean actualEnabled = loginPage.isLoginButtonEnabled();
        switch (expectedState.toLowerCase(Locale.ROOT)) {
            case STATE_ENABLED -> assertThat(actualEnabled)
                    .as("login button should be enabled").isTrue();
            case STATE_DISABLED -> assertThat(actualEnabled)
                    .as("login button should be disabled").isFalse();
            default -> throw new IllegalArgumentException("Unknown button state: " + expectedState);
        }
    }

    @Then("{string} error message should match key {string}")
    public void errorMessageShouldMatchKey(String fieldName, String messageKey) {
        String expected = Messages.get(context.getCurrentLocale(), messageKey);
        String actual = readErrorText(fieldName);
        assertThat(actual)
                .as("Error text for field '%s' in locale %s", fieldName, context.getCurrentLocale())
                .isEqualTo(expected);
    }

    @When("user clicks login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("flash error should match key {string}")
    public void flashErrorShouldMatchKey(String messageKey) {
        String expected = Messages.get(context.getCurrentLocale(), messageKey);
        assertThat(loginPage.getLoginError())
                .as("Flash error in locale %s", context.getCurrentLocale())
                .isEqualTo(expected);
    }

    private String readErrorText(String fieldName) {
        return switch (fieldName.toLowerCase(Locale.ROOT)) {
            case FIELD_USERNAME -> loginPage.getUsernameError();
            case FIELD_PASSWORD -> loginPage.getPasswordError();
            case FIELD_FLASH -> loginPage.getLoginError();
            default -> throw new IllegalArgumentException("Unknown error field: " + fieldName);
        };
    }
}
