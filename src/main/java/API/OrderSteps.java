package API;

import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String GET_INGREDIENTS_ENDPOINT = "/api/ingredients";
    private static final String CREATE_ENDPOINT = "/api/orders";

    private static final String GET_ORDERS_ENDPOINT = "/api/orders";

    public Response sendGetRequestIngredients() {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .get(GET_INGREDIENTS_ENDPOINT);

        return response;

    }

    public Response sendPostRequestOrdersWithoutAuth(Order order) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(order)
                .when()
                .post(CREATE_ENDPOINT);

        return response;
    }

    public Response sendPostRequestOrdersWithAuth(Order order, String token) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", token)
                .body(order)
                .when()
                .post(CREATE_ENDPOINT);

        return response;
    }

    public Response sendGetRequestOrdersWithAuth(String token) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", token)
                .get(GET_ORDERS_ENDPOINT);

        return response;

    }

    public Response sendGetRequestOrdersWithoutAuth() {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .get(GET_ORDERS_ENDPOINT);

        return response;
    }



}
