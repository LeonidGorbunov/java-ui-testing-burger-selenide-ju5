package ru.education_services.qa_stellarburgers.api.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserAuthResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("user")
    private UserData user;

    @Data
    public static class UserData {

        @SerializedName("email")
        private String email;

        @SerializedName("name")
        private String name;
    }
}
