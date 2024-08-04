import API.OrderSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderCreationWithIngredientsWithoutAuthSuccessTest {
    private Order orderCreate;
    private String ingredient;
    private List<String> ingredients;
    OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        Response responseGet = orderSteps.sendGetRequestIngredients();
        ingredient = responseGet.path("data[0]._id").toString();

        ingredients = new ArrayList<>(Collections.singletonList(ingredient));

        orderCreate = new Order()
                .withIngredients(ingredients);
    }

    @Test
    public void createOrderWithIngredientsWithoutAuthSuccess() {
        Response responseCreate = orderSteps.sendPostRequestOrdersWithoutAuth(orderCreate);
        checkStatusCodeInResponse(responseCreate);
        checkNameParameterInResponse(responseCreate);
        checkOrderObjectInResponse(responseCreate);
        checkSuccessParameterInResponse(responseCreate);
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseCreate) {
        responseCreate.then().statusCode(200);
    }

    @Step("Check name parameter is correct")
    public void checkNameParameterInResponse(Response responseCreate) {
        responseCreate.then().body("name", notNullValue());
    }

    @Step("Check order object is correct")
    public void checkOrderObjectInResponse(Response responseCreate) {
        responseCreate.then().body("order.number", greaterThan(0));
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseCreate) {
        responseCreate.then().body("success", equalTo(true));
    }
}
