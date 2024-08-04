import API.OrderSteps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderListWithoutAuthErrorTest {
    OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void getOrderListWithoutAuth() {
        Response response = orderSteps.sendGetRequestOrdersWithoutAuth();
        checkSuccessParameterInResponse(response);
        checkMessageParameterInResponse(response);
    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response response) {
        response.then().assertThat().body("success", equalTo(false));
    }

    @Step("Check message parameter is correct")
    public void checkMessageParameterInResponse(Response response) {
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }
}
