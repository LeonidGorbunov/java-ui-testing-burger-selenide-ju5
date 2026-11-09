package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.CsvSource;
import ru.education_services.qa_stellarburgers.extension.CrossBrowserTest;
import ru.education_services.qa_stellarburgers.pageobject.MainPage;

@Epic("Stellar Burgers: Пользовательский функционал / UI-сценарии")
@Feature("2. Счетчики ингредиентов (параметризованный тест)")
public class IngredientCounterParameterizedTest {

    @CrossBrowserTest
    @DisplayName("Увеличение значения счётчика ингредиента при добавлении его в заказ")
    @Description("Позитивный сценарий увеличения значения счетчика ингредиента")
    @CsvSource({
            "Краторная булка N-200i, 2",
            "Флюоресцентная булка R2-D3, 2",
            "Соус с шипами Антарианского плоскоходца, 1",
            "Соус Spicy-X, 1",
            "Соус фирменный Space Sauce, 1",
            "Соус традиционный галактический, 1",
            "Хрустящие минеральные кольца, 1",
            "Плоды Фалленианского дерева, 1",
            "Кристаллы марсианских альфа-сахаридов, 1",
            "Мини-салат Экзо-Плантаго, 1",
            "Сыр с астероидной плесенью, 1",
            "Филе Люминесцентного тетраодонтимформа, 1",
            "Мясо бессмертных моллюсков Protostomia, 1",
            "Говяжий метеорит (отбивная), 1",
            "Биокотлета из марсианской Магнолии, 1",
    })

    public void shouldIncreaseIngredientCounter(String ingredientName, String expectedCount) {

        new MainPage()
                .openMainPage()
                .dragIngredientToConstructorBasket(ingredientName)
                .checkIngredientCounter(ingredientName, expectedCount);
    }
}
