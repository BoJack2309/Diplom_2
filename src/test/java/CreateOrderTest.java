import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import orders.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserAuthData;
import users.UserRandomizer;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private String userToken;


    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = UserRandomizer.getRandomUser();
        order = new Order();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с корректными ингрединтами")
    public void createOrderWithLoginAndIngredients() {

        order = Order.createIngredients();
        userToken = userClient.createUser(user).extract().path("accessToken");
        userClient.authUser(UserAuthData.withCorrectData(user));
        orderClient.createOrderWithAuth(order, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("order._id", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    public void orderCreateWithLoginAndWithoutIngredients() {
        userToken = userClient.createUser(user).extract().path("accessToken");
        userClient.authUser(UserAuthData.withCorrectData(user));
        orderClient.createOrderWithAuth(order, userToken)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем c корректными ингредиентами")
    public void orderCreateWithoutLoginAndWithIngredients() {
        order = Order.createIngredients();
        orderClient.createOrderWithoutAuth(order)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем без ингредиентов")
    public void orderCreateWithoutLoginAndWithoutIngredients() {
        orderClient.createOrderWithoutAuth(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хешем ингредиентов")
    public void orderCreateWithInvalidHashIngredients() {
        order = Order.createIngredientsWithoutCorrectHash();
        orderClient.createOrderWithoutAuth(order)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}