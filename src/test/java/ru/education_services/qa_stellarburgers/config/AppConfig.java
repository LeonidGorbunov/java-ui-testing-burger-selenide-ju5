package ru.education_services.qa_stellarburgers.config;

import lombok.experimental.UtilityClass;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AppConfig {

    // менее стабильное зеркало: https://stellarburgers.education-services.ru
    public static final String BASE_URL = System.getProperty("site.url", "https://qa-stellarburgers.education-services.ru");

    private static final Set<String> SUPPORTED_BROWSERS = Set.of("chrome", "firefox");

    private static final String BROWSERS = System.getProperty("browsers", "chrome, firefox");

    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));

    public static final long TIMEOUT = Long.parseLong(System.getProperty("timeout", "20000"));

    public static List<String> getBrowsers () {

        List<String> browsers = Arrays.stream(BROWSERS.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(browser -> !browser.isEmpty())
                .peek(browser -> {
                    if (!SUPPORTED_BROWSERS.contains(browser)) {
                        throw new IllegalArgumentException(
                                "Неподдерживаемый браузер: " + browser + ". Доступные варианты: " + SUPPORTED_BROWSERS
                        );
                    }
                })
                .distinct()
                .collect(Collectors.toList());

        if (browsers.isEmpty()) {
            throw new IllegalArgumentException("Не указаны браузеры для запуска тестов. Проверьте параметр -Dbrowsers");
        }

        return browsers;
    }
}
