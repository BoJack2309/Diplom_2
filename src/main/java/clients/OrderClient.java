package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import orders.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String INGREDIENTS = "ingredients";
    private static final String ALL_ORDERS = "orders/all";
    private static final String ORDERS = "orders";

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec()).log().all()
                .when()
                .get(INGREDIENTS)
                .then().log().all();
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getUserOrdersWithAuth(String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token).log().all()
                .get(ORDERS)
                .then().log().all();
    }

    @Step("Получение заказов неавторизованного пользователя")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getSpec()).log().all()
                .get(ORDERS)
                .then().log().all();
    }

    @Step("Получение заказов")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getSpec())
                .get(ALL_ORDERS)
                .then().log().all();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .body(order).log().all()
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order).log().all()
                .post(ORDERS)
                .then().log().all();
    }
}