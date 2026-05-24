package ge.credo.automation.page;

public class LoginPage extends BasePage {

    private static final String USERNAME_INPUT = "usernameInput";
    private static final String PASSWORD_INPUT = "passwordInput";
    private static final String LOGIN_BUTTON = "loginButton";
    private static final String USERNAME_ERROR = "usernameErrorText";
    private static final String PASSWORD_ERROR = "passwordErrorText";
    private static final String LOGIN_ERROR = "flashMessageText";
    private static final String CHANGE_LANGUAGE_BUTTON = "changeLanguageButton";
    private static final String CLOSE_BUTTON = "closeButton";

    public LoginPage enterUserName(String text) {
        findByResourceId(USERNAME_INPUT).sendKeys(text);
        return this;
    }

    public LoginPage clearUserName() {
        findByResourceId(USERNAME_INPUT).clear();
        return this;
    }

    public LoginPage enterPassword(String text) {
        findByResourceId(PASSWORD_INPUT).sendKeys(text);
        return this;
    }

    public LoginPage clearPassword() {
        findByResourceId(PASSWORD_INPUT).clear();
        return this;
    }

    public boolean isLoginButtonEnabled() {
        return findByResourceId(LOGIN_BUTTON).isEnabled();
    }

    public LoginPage clickLoginButton() {
        findByResourceId(LOGIN_BUTTON).click();
        return this;
    }

    public String getUsernameError() {
        return findByResourceId(USERNAME_ERROR).getText();
    }

    public String getPasswordError() {
        return findByResourceId(PASSWORD_ERROR).getText();
    }

    public String getLoginError() {
        return findByResourceId(LOGIN_ERROR).getText();
    }

    public LoginPage changeLanguageViaUI(String languageLabel) {
        findByResourceId(CHANGE_LANGUAGE_BUTTON).click();
        findByText(languageLabel).click();
        findByResourceId(CLOSE_BUTTON).click();
        return this;
    }
}
