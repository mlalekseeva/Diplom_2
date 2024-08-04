import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static utils.Utils.randomString;

public class UserCreationSuccessTest {
    private User userCreate;
    UserSteps userSteps = new UserSteps();
    private String name = randomString(20);
    private String email = randomString(10) + "@yandex.ru";
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userCreate = new User()
                .withEmail(email)
                .withPassword(randomString(12))
                .withName(name);
    }

    @Test
    public void createUserSuccess() {
        Response response = userSteps.sendPostRequestAuthRegister(userCreate);
        token = response.path("accessToken").toString();
        checkStatusCodeInResponse(response);
        checkSuccessParameterInResponse(response);
        checkUserObjectInResponse(response); //Хотелось бы провернить наличие user и password
        checkAccessTokenParameterInResponse(response);
        checkRefreshTokenParameterInResponse(response);
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response response) {
        response.then().statusCode(200);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response response) {
        response.then().assertThat().body("success", equalTo(true));
    }

    @Step("Check user object is correct")
    public void checkUserObjectInResponse(Response response) {
        response.then().assertThat().body("user.email", equalTo(email)).and().body("user.name", equalTo(name));
    }

    @Step("Check access token parameter is not null")
    public void checkAccessTokenParameterInResponse(Response response) {
        response.then().assertThat().body("accessToken", notNullValue());
    }

    @Step("Check refresh token parameter is not null")
    public void checkRefreshTokenParameterInResponse(Response response) {
        response.then().assertThat().body("refreshToken", notNullValue());
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
