package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import ru.education_services.qa_stellarburgers.extension.CrossBrowserTest;
import ru.education_services.qa_stellarburgers.pageobject.MainPage;

@Epic("Stellar Burgers: Пользовательский функционал / UI-сценарии")
@Feature("1. Навигация и модальное окно ингредиента")
public class NavigationAndModalTest  {

    @CrossBrowserTest
    @DisplayName("1. Переход в раздел \"Конструктор\" после клика на кнопку \"Конструктор\" в хэдере")
    @Description("Позитивный сценарий перехода в раздел \"Конструктор\"")
    public void shouldNavigateToConstructorByHeaderClick() {

        new MainPage()
                .openMainPage()
                .clickOrderFeedButton()
                .clickConstructorButton()
                .checkConstructBurgerHeaderVisible();
    }

    @CrossBrowserTest
    @DisplayName("2. Переход в раздел \"Лента заказов\" после клика на кнопку \"Лента заказов\" в хэдере")
    @Description("Позитивный сценарий перехода в раздел \"Лента заказов\"")
    public void shouldNavigateToFeedOrderPageByHeaderClick() {

        new MainPage()
                .openMainPage()
                .clickOrderFeedButton()
                .checkFeedOrderHeaderVisible();
    }

    @CrossBrowserTest
    @DisplayName("3. Отображение модального окна \"Детали ингредиента\" после клика на любой ингредиент в контейнере \"Конструктора\"")
    @Description("Позитивный сценарий отображения модального окна \"Детали ингредиента\"")
    public void shouldNavigateToIngredientDetailsModalByIngredientClick() {

        new MainPage()
                .openMainPage()
                .clickRandomIngredient()
                .checkIngredientDetailsHeaderVisible();
    }

    @CrossBrowserTest
    @DisplayName("4. Закрытие модального окна \"Детали ингредиента\" после клика по \"крестику\"")
    @Description("Позитивный сценарий закрытия модального окна \"Детали ингредиента\"")
    public void shouldCloseIngredientDetailsModalByModalCloseButtonClick() {

        new MainPage()
                .openMainPage()
                .clickRandomIngredient()
                .clickModalCloseButton()
                .checkIngredientDetailsHeaderNotVisible();
    }
}