import API.OrderSteps;
import API.UserSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class OrderCreationWithWrongIngredientsWithAuthErrorTest {

    private User userLogin;
    private Order orderCreate;

    private List<String> ingredients;

    private String token;

    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userLogin = new User()
                .withEmail(randomString(10) + "@yandex.ru")
                .withPassword(randomString(12))
                .withName(randomString(20));

        userSteps.sendPostRequestCreateTestUser(userLogin);
        Response responseLogin = userSteps.sendPostRequestAuthLogin(userLogin);
        token = responseLogin.path("accessToken").toString();

        ingredients = new ArrayList<>(Collections.singletonList("somethingwrong"));

        orderCreate = new Order()
                .withIngredients(ingredients);

    }

    @Test
    public void createOrderWithWrongIngredientsWithAuthError() {


        Response responseCreate = orderSteps.sendPostRequestOrdersWithAuth(orderCreate, token);
        checkStatusCodeInResponse(responseCreate);

    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseCreate) {
        responseCreate.then().statusCode(500);

    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));

    }

}
