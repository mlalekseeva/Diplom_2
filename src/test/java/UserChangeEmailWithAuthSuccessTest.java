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

public class UserChangeEmailWithAuthSuccessTest {
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
                .withEmail(email);
    }

    @Test
    public void userChangeEmailWithAuthSuccess() {
        userSteps.sendPostRequestCreateTestUser(userLogin);
        Response responseLogin = userSteps.sendPostRequestAuthLogin(userLogin);
        token = responseLogin.path("accessToken").toString();
        Response responseChange = userSteps.sendPatchRequestAuthUserWithAuth(userChange, token);
        checkStatusCodeInResponse(responseChange);
        checkSuccessParameterInResponse(responseChange);
        checkUserObjectInResponse(responseChange); //Хотелось бы провернить наличие user и password
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseChange) {
        responseChange.then().statusCode(200);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseChange) {
        responseChange.then().assertThat().body("success", equalTo(true));
    }

    @Step("Check user object is correct")
    public void checkUserObjectInResponse(Response responseChange) {
        responseChange.then().assertThat().body("user.email", equalTo(email));
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
