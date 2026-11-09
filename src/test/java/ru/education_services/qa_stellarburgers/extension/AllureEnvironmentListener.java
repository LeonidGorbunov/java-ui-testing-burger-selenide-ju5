package ru.education_services.qa_stellarburgers.extension;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;
import ru.education_services.qa_stellarburgers.config.AppConfig;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AllureEnvironmentListener implements LauncherSessionListener {

    @Override
    public void launcherSessionClosed(LauncherSession session) {
        Properties properties = new Properties();
        properties.setProperty ("Stand", AppConfig.BASE_URL);
        properties.setProperty ("Browsers", String.join(", ", AppConfig.getBrowsers()));
        properties.setProperty ("Headless", String.valueOf(AppConfig.HEADLESS));
        properties.setProperty ("OS", System.getProperty("os.name"));

        String resultsDir = System.getProperty("allure.results.directory", "target/allure-results");

        try {
            Files.createDirectories(Paths.get(resultsDir));
            try (FileOutputStream out = new FileOutputStream(Paths.get(resultsDir, "environment.properties").toFile())) {
                properties.store(out, "Allure environment properties");
            }
        } catch (IOException e) {
            System.err.println("[Warning] Не удалось записать environment.properties для Allure: " + e.getMessage());
        }
    }
}
