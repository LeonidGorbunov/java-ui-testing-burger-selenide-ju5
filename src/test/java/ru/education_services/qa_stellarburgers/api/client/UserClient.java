package ru.education_services.qa_stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.education_services.qa_stellarburgers.api.models.request.UserLoginRequest;
import ru.education_services.qa_stellarburgers.api.models.request.UserRegisterRequest;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Действие: регистрация нового пользователя, post /api/auth/register")
    public Response actionUserRegister (UserRegisterRequest userRequest) {

        return given(BASE_SPEC)
                .body(userRequest)
                .when()
                .post("/api/auth/register");
    }

    @Step("Действие: удаление пользователя, delete /api/auth/user")
    public Response actionUserDelete (String accessToken) {

        return given(BASE_SPEC)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }

    @Step("Действие: авторизация пользователя, post /api/auth/login")
    public Response actionUserLogin (UserLoginRequest loginRequest) {

        return given(BASE_SPEC)
                .body(loginRequest)
                .when()
                .post("/api/auth/login");
    }
}