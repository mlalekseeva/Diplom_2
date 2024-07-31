import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class UserCreationWithoutNameErrorTest {

    private User userCreate;

    UserSteps userSteps = new UserSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userCreate = new User()
                .withEmail(randomString(10) + "@yandex.ru")
                .withPassword(randomString(12));

    }

    @Test
    public void createUserWithoutNameError() {
        Response response = userSteps.sendPostRequestAuthRegister(userCreate);
        checkStatusCodeInResponse(response);
        checkSuccessParameterInResponse(response);
        checkMessageParameterInResponse(response);
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response response) {
        response.then().statusCode(403);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response response) {
        response.then().assertThat().body("success", equalTo(false));
    }

    @Step("Check message parameter is correct")
    public void checkMessageParameterInResponse(Response response) {
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
