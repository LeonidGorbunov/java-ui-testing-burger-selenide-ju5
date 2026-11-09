package ru.education_services.qa_stellarburgers.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ru.education_services.qa_stellarburgers.config.AppConfig;

public class SelenideExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Configuration.baseUrl = AppConfig.BASE_URL;
        Configuration.headless = AppConfig.HEADLESS;
        Configuration.timeout = AppConfig.TIMEOUT;
        Configuration.pageLoadTimeout = 60000L;
        Configuration.pageLoadStrategy = "eager";
        Configuration.reportsFolder = "target/selenide-reports";
        if (AppConfig.HEADLESS) {
            Configuration.browserSize = "1920x1080";
        } else {
            Configuration.browserSize = null;
        }

        if (!SelenideLogger.hasListener("AllureSelenide")) {
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                    .screenshots(true)
                    .savePageSource(true)
                    .includeSelenideSteps(false)
            );
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Selenide.closeWebDriver();
    }
}