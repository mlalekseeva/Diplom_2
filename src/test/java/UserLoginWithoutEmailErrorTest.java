import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class UserLoginWithoutEmailErrorTest {


    private User userLogin;

    UserSteps userSteps = new UserSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userLogin = new User()
                .withPassword(randomString(12))
                .withName(randomString(20));
    }

    @Test
    public void loginUserWithoutEmailError() {
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
