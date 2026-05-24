package ge.credo.automation.context;

import ge.credo.automation.config.SupportedLocale;

public class TestContext {

    private SupportedLocale currentLocale = SupportedLocale.EN;

    public SupportedLocale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(SupportedLocale currentLocale) {
        this.currentLocale = currentLocale;
    }
}
