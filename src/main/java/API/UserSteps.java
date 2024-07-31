package API;

import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    private static final String CREATE_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String CHANGE_ENDPOINT = "/api/auth/user";
    private static final String DELETE_ENDPOINT = "/api/auth/user";

    public Response sendPostRequestAuthRegister(User userCreate) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(userCreate)
                .when()
                .post(CREATE_ENDPOINT);

        return response;
    }

    public void sendPostRequestCreateTestUser(User userCreate) {
        given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(userCreate)
                .when()
                .post(CREATE_ENDPOINT);
    }

    public Response sendPostRequestAuthLogin(User userLogin) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(userLogin)
                .when()
                .post(LOGIN_ENDPOINT);

        return response;
    }

    public Response sendPatchRequestAuthUserWithAuth(User userChange, String token) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", token)
                .body(userChange)
                .when()
                .patch(CHANGE_ENDPOINT);

        return response;
    }

    public Response sendPatchRequestAuthUserWithoutAuth(User userChange) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(userChange)
                .when()
                .patch(CHANGE_ENDPOINT);

        return response;
    }

    public Response sendDeleteRequestAuthUser(String token) {
        Response response = given()
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", token)
                .when()
                .delete(DELETE_ENDPOINT);

        return response;
    }




}
