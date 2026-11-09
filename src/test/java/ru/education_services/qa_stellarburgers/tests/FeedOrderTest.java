package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.education_services.qa_stellarburgers.api.client.UserClient;
import ru.education_services.qa_stellarburgers.api.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.api.models.request.UserRegisterRequest;
import ru.education_services.qa_stellarburgers.api.models.response.UserAuthResponse;
import ru.education_services.qa_stellarburgers.api.steps.UserTeardownSteps;
import ru.education_services.qa_stellarburgers.extension.CrossBrowserTest;
import ru.education_services.qa_stellarburgers.pageobject.FeedOrderPage;
import ru.education_services.qa_stellarburgers.pageobject.MainPage;
import ru.education_services.qa_stellarburgers.pageobject.OrderCreatedModal;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Stellar Burgers: Пользовательский функционал / UI-сценарии")
@Feature("3. Раздел \"Лента Заказов\"")
public class FeedOrderTest {

    private UserRegisterRequest userRegisterRequest;
    private String accessToken;
    private String refreshToken;
    private final UserTeardownSteps userTeardownSteps = new UserTeardownSteps();

    @BeforeEach
    public void prepareTestData () {
        UserClient userClient = new UserClient();
        userRegisterRequest = UserDataGenerator.getDefaultUser();
        UserAuthResponse userAuthResponse = userClient.actionUserRegister(userRegisterRequest).as(UserAuthResponse.class);
        accessToken = userAuthResponse.getAccessToken();
        refreshToken = userAuthResponse.getRefreshToken();
    }

    @CrossBrowserTest
    @DisplayName("Проверка увеличения значений счетчиков \"Выполнено за все время\" и \"Выполнено за сегодня\", и " +
            "появления номера заказа в секции \"В работе:\"")
    @Description("Позитивный сценарий создания нового заказа и проверки увеличения значений счетчиков \"Выполнено за все время\" и" +
            " \"Выполнено за сегодня\", а также появления номера оформленного заказа в секции \"В работе:\"")
    public void shouldTotalAndTodayCountersIncreaseAndOrderNumberAppearIntoOrderInWorkSection() {

        int totalBefore = new MainPage()
                .openMainPage()
                .authorizeWithTokens(accessToken, refreshToken)
                .clickOrderFeedButton()
                .getTotalCounterValue();

        int todayBefore = new FeedOrderPage()
                .getTodayCounterValue();

        int createdOrderNumber = new FeedOrderPage()
                .clickConstructorButton()
                .dragIngredientToConstructorBasket("Краторная булка N-200i")
                .dragIngredientToConstructorBasket("Хрустящие минеральные кольца")
                .clickOrderCreateButton()
                .getOrderNumber();

        new OrderCreatedModal()
                .clickModalCloseButton();

        FeedOrderPage feedOrderPage = new MainPage().clickOrderFeedButton();

        int totalAfter = feedOrderPage.getTotalCounterValue();

        int todayAfter = feedOrderPage.getTodayCounterValue();

        Allure.step("⚠️ ПРОВЕРКА: изменения в ленте заказов после оформления заказа #" + createdOrderNumber, () ->
                assertAll(
                        () -> Allure.step(String.format("Счётчик 'Выполнено за всё время' увеличился (было: %d, стало: %d)", totalBefore, totalAfter),
                                () -> assertTrue(totalAfter > totalBefore)),
                        () -> Allure.step(String.format("Счётчик 'Выполнено за сегодня' увеличился (было: %d, стало: %d)", todayBefore, todayAfter),
                                () -> assertTrue(todayAfter > todayBefore)),
                        () -> Allure.step(String.format("Номер заказа #%d отображается в разделе 'В работе'", createdOrderNumber),
                                () -> assertTrue(feedOrderPage.isOrderInWork(createdOrderNumber)))
                )
        );
    }

    @AfterEach
    public void deleteUserAfterTest() {
        userTeardownSteps.deleteUserSafely(accessToken, userRegisterRequest.getEmail(), userRegisterRequest.getPassword());
    }
}
