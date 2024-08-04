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
import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.randomString;

public class OrderCreationWithoutIngredientsAndWithAuthErrorTest {
    private User userLogin;
    private Order orderCreate;
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

        orderCreate = new Order();
    }

    @Test
    public void createOrderWithoutIngredientsWithoutAuthError() {
        Response responseCreate = orderSteps.sendPostRequestOrdersWithAuth(orderCreate, token);
        checkStatusCodeInResponse(responseCreate);
        checkMessageParameterInResponse(responseCreate);
        checkSuccessParameterInResponse(responseCreate);
    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseCreate) {
        responseCreate.then().statusCode(400);
    }

    @Step("Check message parameter is correct")
    public void checkMessageParameterInResponse(Response responseCreate) {
        responseCreate.then().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseCreate) {
        responseCreate.then().body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
