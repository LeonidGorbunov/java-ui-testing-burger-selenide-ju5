package ru.education_services.qa_stellarburgers.extension;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.support.AnnotationSupport;
import ru.education_services.qa_stellarburgers.config.AppConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CrossBrowserProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return AnnotationSupport.isAnnotated(context.getRequiredTestMethod(), CrossBrowserTest.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        Optional<CsvSource> csvSource = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CsvSource.class);
        if (csvSource.isEmpty()) {
            return AppConfig.getBrowsers().stream()
                    .map(browser -> invocationContext(browser, new String[0]));
        }

        String[] rows = csvSource.get().value();
        return AppConfig.getBrowsers().stream()
                .flatMap(browser -> Arrays.stream(rows)
                        .map(row -> invocationContext(browser, parseCsvRow(row))));
    }

    private String[] parseCsvRow(String row) {
        return Arrays.stream(row.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    private TestTemplateInvocationContext invocationContext(String browser, String[] args) {

        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                if (args.length == 0) {
                    return "[" + browser + "]";
                }
                return "[" + browser + "] " + String.join("  0->", args);
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                // 1. Расширение настройки браузера и разделения истории в Allure
                BeforeEachCallback browserSetupExtension = context -> {
                    Configuration.browser = browser;
                    Allure.parameter("Browser", browser);

                    String suffix = args.length == 0
                            ? "[" + browser + "]"
                            : "[" + browser + " | " + String.join(", ", args) + "]";

                    if (args.length > 0) {
                        Allure.parameter("Test data:", String.join(" | ", args));
                    }

                    Allure.getLifecycle().updateTestCase(testCase -> {
                        testCase.setFullName(testCase.getFullName() + " " + suffix);
                        testCase.setHistoryId(testCase.getHistoryId() + "_" + suffix);
                    });
                };

                // 2. Расширение подстановки параметров из @CsvSource в аргументы теста
                ParameterResolver parameterResolver = new ParameterResolver() {

                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                        return parameterContext.getIndex() < args.length;
                    }

                    @Override
                    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                        String rawValue = args[parameterContext.getIndex()];
                        Class<?> targetType = parameterContext.getParameter().getType();

                        if (targetType == int.class || targetType == Integer.class) {
                            return Integer.parseInt(rawValue);
                        }
                        return rawValue;
                    }
                };

                return List.of(browserSetupExtension, parameterResolver);
            }
        };
    }
}
