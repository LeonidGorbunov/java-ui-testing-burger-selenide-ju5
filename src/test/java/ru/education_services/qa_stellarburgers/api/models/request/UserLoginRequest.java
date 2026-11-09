package ru.education_services.qa_stellarburgers.api.models.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;
}
