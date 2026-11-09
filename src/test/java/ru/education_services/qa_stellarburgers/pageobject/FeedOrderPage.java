package ru.education_services.qa_stellarburgers.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.education_services.qa_stellarburgers.utils.WaitUtils.waitForPrecondition;

public class FeedOrderPage {

    //локаторы
    //кнопка "Конструктор" в хэдере
    private final SelenideElement constructorButton = $$("a[class*='AppHeader_header__link']").findBy(text("Конструктор"));
    //заголовок "Лента заказов"
    private final SelenideElement feedOrderHeader = $$("h1").findBy(text("Лента заказов"));
    //счетчик "Выполнено за все время"
    private final SelenideElement totalCounter =
            $x("//p[contains(normalize-space(), 'Выполнено за все время')]/following-sibling::p");
    //счетчик "Выполнено за сегодня"
    private final SelenideElement todayCounter =
            $x("//p[contains(normalize-space(), 'Выполнено за сегодня')]/following-sibling::p");
    //коллекция локаторов номеров заказов в секции "В работе:"
    private final ElementsCollection ordersInWork =
            $$x("//p[contains(normalize-space(), 'В работе')]/following-sibling::ul/li");

    @Step("Действие: клик по кнопке \"Конструктор\" в хэдере")
    public MainPage clickConstructorButton() {
        constructorButton.click();
        return new MainPage();
    }

    @Step("⚠️ ПРОВЕРКА: отображается заголовок \"Лента заказов\" на странице \"Лента заказов\"")
    public void checkFeedOrderHeaderVisible() {
        feedOrderHeader
                .shouldBe(visible.because("Заголовок \"Лента заказов\" должен отображаться после клика на кнопку " +
                        "\"Лента заказов\" в хэдере"));
    }

    @Step("Действие: получение значения счетчика \"Выполнено за все время\"")
    public int getTotalCounterValue() {
        waitForPrecondition(() -> totalCounter.shouldHave(matchText("\\d+")));
        String value = totalCounter
                .getText()
                .trim();
        return Integer.parseInt(value);
    }

    @Step("Действие: получение значения счетчика \"Выполнено за сегодня\"")
    public int getTodayCounterValue() {
        waitForPrecondition(() -> todayCounter.shouldHave(matchText("\\d+")));
        String value = todayCounter
                .getText()
                .trim();
        return Integer.parseInt(value);
    }

    @Step("Действие: проверка наличия номера созданного заказа в секции \"В работе:\"")
    public boolean isOrderInWork(int orderNumber) {
        try {
            ordersInWork.findBy(text(String.valueOf(orderNumber))).shouldBe(visible);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}
