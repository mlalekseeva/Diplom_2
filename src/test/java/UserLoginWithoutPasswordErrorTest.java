import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class UserLoginWithoutPasswordErrorTest {

    private User userCreate;
    private User userLogin;

    UserSteps userSteps = new UserSteps();

    private String email;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userCreate = new User()
                .withEmail(randomString(10) + "@yandex.ru")
                .withPassword(randomString(12))
                .withName(randomString(20));

        userLogin = new User()
                .withEmail(email);


    }

    @Test
    public void loginUserWithoutPasswordError() {
        Response responseCreate = userSteps.sendPostRequestAuthRegister(userCreate);
        email = responseCreate.path("user.email").toString();
        Response response = userSteps.sendPostRequestAuthLogin(userLogin);
        checkStatusCodeInResponse(response);
        checkSuccessParameterInResponse(response);
        checkMessageParameterInResponse(response);

    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response response) {
        response.then().statusCode(401);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response response) {
        response.then().assertThat().body("success", equalTo(false));
    }

    @Step("Check message parameter is correct")
    public void checkMessageParameterInResponse(Response response) {
        response.then().assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
