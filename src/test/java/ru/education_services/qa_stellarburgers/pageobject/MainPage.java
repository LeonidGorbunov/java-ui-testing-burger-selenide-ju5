package ru.education_services.qa_stellarburgers.pageobject;

import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import ru.education_services.qa_stellarburgers.config.AppConfig;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.DragAndDropOptions.to;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.util.concurrent.ThreadLocalRandom.current;
import static ru.education_services.qa_stellarburgers.utils.WaitUtils.waitForPrecondition;

public class MainPage {

    //локаторы
    //кнопка "Лента заказов" в хэдере
    private final SelenideElement orderFeedButton = $$("a[class*='AppHeader_header__link']").findBy(text("Лента заказов"));
    //заголовок "Соберите бургер"
    private final SelenideElement constructBurgerHeader = $$("h1").findBy(text("Соберите бургер"));
    //коллекция локаторов ингредиентов
    private final ElementsCollection ingredientCards = $$("a[class*='BurgerIngredient_ingredient']");
    //заголовок "Детали ингредиента"
    private final SelenideElement ingredientDetailsHeader = $$("h2").findBy(text("Детали ингредиента"));
    //модальное окно оверлея
    private final SelenideElement modalOverlay = $("div[class*='Modal_modal_overlay']");
    //строковая переменная для динамического локатора ингредиента
    private static final String INGREDIENT_CARD_XPATH = "//p[normalize-space()='%s']/ancestor::a";
    //строковая переменная для динамического локатора счетчика ингредиента
    private static final String INGREDIENT_COUNTER_CSS = "p[class*='counter_counter__num']";
    //локатор области корзины
    private final SelenideElement constructorBasket = $("ul[class*='BurgerConstructor_basket__list']");
    //кнопка "Оформить заказ"
    private final SelenideElement orderCreateButton =
            $$("button[class*='button_button_type_primary']").findBy(text("Оформить заказ"));

    //хелпер для создания локатора ингредиента
    private SelenideElement getIngredientCard(String ingredientName) {
        return $x(String.format(INGREDIENT_CARD_XPATH, ingredientName));
    }

    //хелпер для создания локатора счетчика ингредиента
    private SelenideElement getIngredientCounter(String ingredientName) {
        return getIngredientCard(ingredientName).$(INGREDIENT_COUNTER_CSS);
    }

    @Step("Действие: открыть главную страницу стенда Stellar Burgers")
    public MainPage openMainPage () {
        Selenide.open("/");
        if (!AppConfig.HEADLESS) {
            getWebDriver().manage().window().maximize();
        }
        waitForPrecondition(() -> modalOverlay.shouldNotBe(visible));
        return this;
    }

    @Step("Действие: клик по кнопке \"Лента заказов\" в хэдере")
    public FeedOrderPage clickOrderFeedButton() {
        orderFeedButton.click();
        return new FeedOrderPage();
    }

    @Step("⚠️ ПРОВЕРКА: отображается заголовок \"Соберите бургер\" на главной странице")
    public void checkConstructBurgerHeaderVisible() {
        constructBurgerHeader
                .shouldBe(visible.because("Заголовок \"Соберите бургер\" должен отображаться после клика на кнопку " +
                        "\"Конструктор\" в хэдере"));
    }

    @Step("Действие: клик по случайному ингредиенту в контейнере \"Конструктора\"")
    public IngredientDetailsModal clickRandomIngredient() {
        waitForPrecondition(() -> ingredientCards.shouldBe(CollectionCondition.sizeGreaterThan(5)));
        int randomIndex = current().nextInt(ingredientCards.size());
        SelenideElement randomIngredient = ingredientCards.get(randomIndex);
        randomIngredient.scrollIntoView("{block: 'center'}");
        randomIngredient.click();
        return new IngredientDetailsModal();
    }

    @Step("⚠️ ПРОВЕРКА: не отображается заголовок модального окна \"Детали ингредиента\"")
    public void checkIngredientDetailsHeaderNotVisible() {
        ingredientDetailsHeader
                .shouldNotBe(visible.because("Заголовок модального окна \"Детали ингредиента\" не должен отображаться " +
                        "после закрытия этого модального окна кликом по \"крестику\""));
    }

    @Step("Действие: drag&drop ингредиента \"{ingredientName}\" в корзину конструктора")
    public MainPage dragIngredientToConstructorBasket(String ingredientName) {
        waitForPrecondition(() -> ingredientCards.shouldBe(CollectionCondition.sizeGreaterThan(5)));
        getIngredientCard(ingredientName)
                .scrollIntoView("{block: 'center'}")
                .dragAndDrop(to(constructorBasket));
        return this;
    }

    @Step("⚠️ ПРОВЕРКА: счётчик ингредиента \"{ingredientName}\" равен \"{expectedCount}\"")
    public void checkIngredientCounter(String ingredientName, String expectedCount) {
        getIngredientCounter(ingredientName)
                .shouldHave(text(expectedCount)
                        .because("Счётчик ингредиента должен увеличиться после добавления этого ингредиента в корзину!"));
    }

    @Step("Действие: авторизация пользователя в браузере через токены")
    public MainPage authorizeWithTokens(String accessToken, String refreshToken) {
        Selenide.localStorage().setItem("accessToken", accessToken);
        Selenide.localStorage().setItem("refreshToken", refreshToken);
        Selenide.refresh();
        waitForPrecondition(() -> modalOverlay.shouldNotBe(visible));
        return this;
    }

    @Step("Действие: клик по кнопке \"Оформить заказ\" в хэдере")
    public OrderCreatedModal clickOrderCreateButton() {
        orderCreateButton.click();
        return new OrderCreatedModal();
    }
}