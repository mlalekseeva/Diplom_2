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
import static org.hamcrest.CoreMatchers.notNullValue;
import static utils.Utils.randomString;

public class OrderListWithAuthSuccessTest {
    private User userLogin;
    private Order orderCreate;
    private String ingredient;
    private List<String> ingredients;
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    private String token;
    private int number;

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
        Response responseGet = orderSteps.sendGetRequestIngredients();
        ingredient = responseGet.path("data[0]._id").toString();

        ingredients = new ArrayList<>(Collections.singletonList(ingredient));

        orderCreate = new Order()
                .withIngredients(ingredients);

        Response responseCreate = orderSteps.sendPostRequestOrdersWithAuth(orderCreate, token);
        number = responseCreate.path("order.number");
    }

    @Test
    public void getOrderListWithAuth() {
        Response responseOrderList = orderSteps.sendGetRequestOrdersWithAuth(token);
        checkStatusCodeInResponse(responseOrderList);
        checkSuccessParameterInResponse(responseOrderList);
        checkOrdersObjectIngredientsParameterInResponse(responseOrderList);
        checkOrdersObjectIdParameterInResponse(responseOrderList);
        checkOrdersObjectStatusParameterInResponse(responseOrderList);
        checkOrdersObjectNumberParameterInResponse(responseOrderList);
        checkOrdersObjectCreatedAtParameterInResponse(responseOrderList);
        checkOrdersObjectUpdatedAtParameterInResponse(responseOrderList);
        checkTotalParameterInResponse(responseOrderList);
        checkTotalTodayParameterInResponse(responseOrderList);
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseOrderList) {
        responseOrderList.then().statusCode(200);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("success", equalTo(true));
    }

    @Step("Check ingredients parameter in orders object is correct")
    public void checkOrdersObjectIngredientsParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0].ingredients[0]", equalTo(ingredient));
    }

    @Step("Check id parameter in orders object is correct")
    public void checkOrdersObjectIdParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0]._id", notNullValue());
    }

    @Step("Check status parameter in orders object is correct")
    public void checkOrdersObjectStatusParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0].status", notNullValue());
    }

    @Step("Check number parameter in orders object is correct")
    public void checkOrdersObjectNumberParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0].number", equalTo(number));
    }

    @Step("Check createdAt parameter in orders object is correct")
    public void checkOrdersObjectCreatedAtParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0].createdAt", notNullValue());
    }

    @Step("Check updatedAt parameter in orders object is correct")
    public void checkOrdersObjectUpdatedAtParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("orders[0].updatedAt", notNullValue());
    }

    @Step("Check total parameter is correct")
    public void checkTotalParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("total", notNullValue());
    }

    @Step("Check totalToday parameter is correct")
    public void checkTotalTodayParameterInResponse(Response responseOrderList) {
        responseOrderList.then().assertThat().body("totalToday", notNullValue());
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
