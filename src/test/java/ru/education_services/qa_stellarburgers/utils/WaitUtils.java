package ru.education_services.qa_stellarburgers.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WaitUtils {

    public static void waitForPrecondition(Runnable waitAction) {
        try {
            waitAction.run();
        } catch (AssertionError e) {
            throw new IllegalStateException("Инфраструктурный сбой: предусловие не выполнено. " + e.getMessage());
        }
    }
}