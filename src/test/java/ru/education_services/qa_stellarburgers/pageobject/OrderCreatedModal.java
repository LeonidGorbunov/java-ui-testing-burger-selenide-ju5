package ru.education_services.qa_stellarburgers.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Selenide.*;
import static ru.education_services.qa_stellarburgers.utils.WaitUtils.waitForPrecondition;

public class OrderCreatedModal {

    //номер заказа модального окна "Идентификатор заказа"
    private final SelenideElement orderNumber =
            $x("//p[contains(normalize-space(), 'идентификатор заказа')]/preceding-sibling::h2");
    //кнопка "крестик" закрытия окна модального окна "Идентификатор заказа"
    private final SelenideElement modalCloseButton =
            $("section[class*='Modal_modal_opened'] button[class*='Modal_modal__close']");

    @Step("Действие: клик по \"крестику\" модального окна \"Идентификатор заказа\"")
    public MainPage clickModalCloseButton() {
        modalCloseButton.click();
        return new MainPage();
    }

    @Step("Действие: получение номера заказа модального окна \"Идентификатор заказа\"")
    public int getOrderNumber() {
        waitForPrecondition(() -> orderNumber
                .shouldHave(matchText("\\d+"))
                .shouldNotHave(exactText("9999")));
        String value = orderNumber
                .getText()
                .trim();
        return Integer.parseInt(value);
    }
}
