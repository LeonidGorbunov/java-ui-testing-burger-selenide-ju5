package ru.education_services.qa_stellarburgers.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class IngredientDetailsModal {

    //заголовок "Детали ингредиента"
    private final SelenideElement ingredientDetailsHeader = $$("h2").findBy(text("Детали ингредиента"));
    //кнопка "крестик" закрытия окна "Детали ингредиента"
    private final SelenideElement modalCloseButton = $("section[class*='Modal_modal_opened'] button[class*='Modal_modal__close']");

    @Step("⚠️ ПРОВЕРКА: отображается заголовок модального окна \"Детали ингредиента\"")
    public void checkIngredientDetailsHeaderVisible() {
        ingredientDetailsHeader
                .shouldBe(visible.because("Заголовок модального окна \"Детали ингредиента\" должен отображаться " +
                        "после клика на любой ингредиент в контейнере \"Конструктора\""));
    }

    @Step("Действие: клик по \"крестику\" модального окна \"Детали ингредиента\"")
    public MainPage clickModalCloseButton() {
        modalCloseButton.click();
        return new MainPage();
    }
}
