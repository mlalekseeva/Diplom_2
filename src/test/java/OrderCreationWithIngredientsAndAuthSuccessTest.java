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

public class OrderCreationWithIngredientsAndAuthSuccessTest {

    private User userLogin;
    private Order orderCreate;
    private String ingredient;

    private List<String> ingredients;

    private String token;
    private String ingredientsName;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;

    private String ownerName;
    private String ownerEmail;

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
        ownerName = responseLogin.path("user.name").toString();
        ownerEmail = responseLogin.path("user.email").toString();

        Response responseGet = orderSteps.sendGetRequestIngredients();
        ingredient = responseGet.path("data[0]._id").toString();
        ingredientsName = responseGet.path("data[0].name").toString();
        type = responseGet.path("data[0].type").toString();
        proteins = responseGet.path("data[0].proteins");
        fat = responseGet.path("data[0].fat");
        carbohydrates = responseGet.path("data[0].carbohydrates");
        calories = responseGet.path("data[0].calories");
        price = responseGet.path("data[0].price");
        image = responseGet.path("data[0].image").toString();

        ingredients = new ArrayList<>(Collections.singletonList(ingredient));

        orderCreate = new Order()
                .withIngredients(ingredients);

    }

    @Test
    public void createOrderWithIngredientsAndAuthSuccess() {


        Response responseCreate = orderSteps.sendPostRequestOrdersWithAuth(orderCreate, token);
        checkStatusCodeInResponse(responseCreate);
        checkSuccessParameterInResponse(responseCreate);
        checkNameParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectIdParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectNameParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectTypeParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectProteinsParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectFatParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectCarbohydratesParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectCaloriesParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectPriceParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectImageParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectImageMobileParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectImageLargeParameterInResponse(responseCreate);
        checkOrderObjectIngredientsObjectVParameterInResponse(responseCreate);
        checkOrderObjectIdParameterInResponse(responseCreate);
        checkOrderObjectOwnerObjectNameParameterInResponse(responseCreate);
        checkOrderObjectOwnerObjectEmailParameterInResponse(responseCreate);
        checkOrderObjectOwnerObjectCreatedAtParameterInResponse(responseCreate);
        checkOrderObjectOwnerObjectUpdatedAtParameterInResponse(responseCreate);
        checkOrderObjectStatusParameterInResponse(responseCreate);
        checkOrderObjectNameParameterInResponse(responseCreate);
        checkOrderObjectCreatedAtParameterInResponse(responseCreate);
        checkOrderObjectUpdatedAtParameterInResponse(responseCreate);
        checkOrderObjectNumberParameterInResponse(responseCreate);
        checkOrderObjectPriceParameterInResponse(responseCreate);




    }

    @Step("Check status code is correct")
    public void checkStatusCodeInResponse(Response responseCreate) {
        responseCreate.then().statusCode(200);

    }

    @Step("Check success parameter is correct")
    public void checkSuccessParameterInResponse(Response responseCreate) {
        responseCreate.then().body("success", equalTo(true));
    }

    @Step("Check name parameter is correct")
    public void checkNameParameterInResponse(Response responseCreate) {
        responseCreate.then().body("name", notNullValue());
    }

    @Step("Check ingredients id parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectIdParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0]._id", equalTo(ingredient));
    }

    @Step("Check ingredients name parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectNameParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].name", equalTo(ingredientsName));
    }

    @Step("Check ingredients type parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectTypeParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].type", equalTo(type));
    }

    @Step("Check ingredients proteins parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectProteinsParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].proteins", equalTo(proteins));
    }

    @Step("Check ingredients fat parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectFatParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].fat", equalTo(fat));
    }

    @Step("Check ingredients carbohydrates parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectCarbohydratesParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].carbohydrates", equalTo(carbohydrates));
    }

    @Step("Check ingredients calories parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectCaloriesParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].calories", equalTo(calories));
    }

    @Step("Check ingredients price parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectPriceParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].price", equalTo(price));
    }

    @Step("Check ingredients image parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectImageParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].image", equalTo(image));
    }

    @Step("Check ingredients image_mobile parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectImageMobileParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].image_mobile", notNullValue());
    }

    @Step("Check ingredients image_large parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectImageLargeParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].image_large", notNullValue());
    }

    @Step("Check ingredients __v parameter in order object is correct")
    public void checkOrderObjectIngredientsObjectVParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.ingredients[0].__v", notNullValue());
    }

    @Step("Check id parameter in order object is correct")
    public void checkOrderObjectIdParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order._id", notNullValue());
    }

    @Step("Check owner name parameter in order object is correct")
    public void checkOrderObjectOwnerObjectNameParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.owner.name", equalTo(ownerName));
    }

    @Step("Check owner email parameter in order object is correct")
    public void checkOrderObjectOwnerObjectEmailParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.owner.email", equalTo(ownerEmail));
    }

    @Step("Check owner createdAt parameter in order object is correct")
    public void checkOrderObjectOwnerObjectCreatedAtParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.owner.createdAt", notNullValue());
    }

    @Step("Check owner updatedAt parameter in order object is correct")
    public void checkOrderObjectOwnerObjectUpdatedAtParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.owner.updatedAt", notNullValue());
    }

    @Step("Check status parameter in order object is correct")
    public void checkOrderObjectStatusParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.status", notNullValue());
    }

    @Step("Check name parameter in order object is correct")
    public void checkOrderObjectNameParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.name", notNullValue());
    }

    @Step("Check createdAt parameter in order object is correct")
    public void checkOrderObjectCreatedAtParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.createdAt", notNullValue());
    }

    @Step("Check updatedAt parameter in order object is correct")
    public void checkOrderObjectUpdatedAtParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.updatedAt", notNullValue());
    }

    @Step("Check number parameter in order object is correct")
    public void checkOrderObjectNumberParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.number", notNullValue());
    }

    @Step("Check price parameter in order object is correct")
    public void checkOrderObjectPriceParameterInResponse(Response responseCreate) {
        responseCreate.then().body("order.price", notNullValue());
    }

    @After
    public void tearDown() {
        Response responseDelete = userSteps.sendDeleteRequestAuthUser(token);
        responseDelete.then().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));

    }


}
