package ru.education_services.qa_stellarburgers.api.data;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import ru.education_services.qa_stellarburgers.api.models.request.UserRegisterRequest;
import java.util.Locale;

@UtilityClass
public class UserDataGenerator {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    public static UserRegisterRequest getDefaultUser() {

        String uniqueEmail = FAKER.number().digits(5) + FAKER.internet().emailAddress();
        String uniquePassword = FAKER.text().text(8, 12, true, false, true);
        String uniqueName = FAKER.space().star();

        return new UserRegisterRequest(uniqueEmail, uniquePassword, uniqueName);
    }
}