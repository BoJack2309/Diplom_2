import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import orders.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserAuthData;
import users.UserRandomizer;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class GetOrderTest {

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
        order = Order.createIngredients();
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthTest() {
        userToken = userClient.createUser(user).extract().path("accessToken");
        userClient.authUser(UserAuthData.withCorrectData(user));
        ValidatableResponse responseOrder = orderClient.createOrderWithAuth(order, userToken);
        String nameBurger = responseOrder.extract().path("name");
        orderClient.getUserOrdersWithAuth(userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("orders[0].name", equalTo(nameBurger));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithoutAuthTest() {
        orderClient.createOrderWithoutAuth(order);
        orderClient.getUserOrdersWithoutAuth()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}