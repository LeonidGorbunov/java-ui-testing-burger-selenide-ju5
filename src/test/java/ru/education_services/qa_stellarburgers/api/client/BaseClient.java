package ru.education_services.qa_stellarburgers.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.education_services.qa_stellarburgers.config.AppConfig;

public abstract class BaseClient {

    protected static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(AppConfig.BASE_URL)
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .log(LogDetail.URI)
            .log(LogDetail.BODY)
            .build();
}