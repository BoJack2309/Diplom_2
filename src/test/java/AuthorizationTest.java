import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserAuthData;
import users.UserRandomizer;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class AuthorizationTest {

    private User user;
    private UserClient userClient;
    private UserAuthData userAuthData;
    private String userToken;

    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = UserRandomizer.getRandomUser();
        userClient = new UserClient();
        userToken = userClient.createUser(user).extract().path("accessToken");
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя с корректными данными")
    public void AuthorizationTest() {
        userClient.authUser(UserAuthData.withCorrectData(user))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация с некорректной почтой")
    @Description("Проверка авторизации пользователя с некорректной почтой")
    public void AuthorizationWithoutCorrectEmailTest() {
        userAuthData = UserAuthData.withIncorrectEmail(user);
        userClient.authUser(userAuthData)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void AuthorizationWithoutCorrectPasswordTest() {
        userAuthData = UserAuthData.withIncorrectPassword(user);
        userClient.authUser(userAuthData)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

}