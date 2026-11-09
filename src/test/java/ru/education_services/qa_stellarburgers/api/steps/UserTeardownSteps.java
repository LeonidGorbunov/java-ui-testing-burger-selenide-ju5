package ru.education_services.qa_stellarburgers.api.steps;

import io.qameta.allure.Step;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.education_services.qa_stellarburgers.api.client.UserClient;
import ru.education_services.qa_stellarburgers.api.models.request.UserLoginRequest;

public class UserTeardownSteps {

    private final UserClient userClient = new UserClient();

    @Step("TEARDOWN: очистка данных пользователя на бэкенде (с преавторизацией при необходимости)")
    public void deleteUserSafely(String accessToken, String email, String password) {

        String tokenForDeletion = accessToken;

        if (tokenForDeletion == null && email != null && password != null) {
            try {
                Response loginResponse = userClient.actionUserLogin(new UserLoginRequest(email, password));

                if (loginResponse.getStatusCode() == HttpStatus.SC_OK) {
                    tokenForDeletion = loginResponse.path("accessToken");
                } else {
                    System.err.println("[Warning] Не удалось получить токен. Бэк вернул статус: "
                            + loginResponse.getStatusCode());
                }

            } catch (JsonPathException e) {
                System.err.println("[Error] Бэкенд вернул некорректный формат ответа при авторизации: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[Error] Сетевая ошибка при попытке авторизации: " + e.getMessage());
            }
        }

        if (tokenForDeletion != null) {
            try {
                Response deleteResponse = userClient.actionUserDelete(tokenForDeletion);

                if (deleteResponse.getStatusCode() != HttpStatus.SC_ACCEPTED &&
                        deleteResponse.getStatusCode() != HttpStatus.SC_OK) {
                    System.err.println("[Warning] Пользователь не удален. Статус ответа бэкенда: "
                            + deleteResponse.getStatusCode());
                }

            } catch (JsonPathException e) {
                System.err.println("[Error] Ошибка парсинга ответа при удалении пользователя: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[Error] Сетевая ошибка при удалении пользователя: " + e.getMessage());
            }
        }
    }
}