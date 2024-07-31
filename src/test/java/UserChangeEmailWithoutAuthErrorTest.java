import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class UserChangeEmailWithoutAuthErrorTest {

    private User userLogin;
    private User userChange;

    UserSteps userSteps = new UserSteps();

    private String email = randomString(10) + "@yandex.ru";
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userLogin = new User()
                .withEmail(randomString(10) + "@yandex.ru")
                .withPassword(randomString(12))
                .withName(randomString(20));

        userChange = new User()
                .withName(email);


    }

    @Test
    public void userChangeEmailWithoutAuthError() {
        userSteps.sendPostRequestCreateTestUser(userLogin);
        Response responseLogin = userSteps.sendPostRequestAuthLogin(userLogin);
        token = responseLogin.path("accessToken").toString();
        Response responseChange = userSteps.sendPatchRequestAuthUserWithoutAuth(userChange);
        checkStatusCodeInResponse(responseChange);
        checkSuccessParameterInResponse(responseChange);
        checkMessageParameterInResponse(responseChange);

    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseChange) {
        responseChange.then().statusCode(401);

    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseChange) {
        responseChange.then().assertThat().body("success", equalTo(false));
    }

    @Step("Check message parameter is correct")
    public void checkMessageParameterInResponse(Response responseChange) {
        responseChange.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));

    }
}
