import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserAuthData;
import users.UserRandomizer;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class EditUserTest {

    private User user;
    private UserClient userClient;
    private String userToken;
    private User newUser;

    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = UserRandomizer.getRandomUser();
        userClient = new UserClient();
        userToken = userClient.createUser(user).extract().path("accessToken");
        newUser = UserRandomizer.getRandomUser();
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Редактирование пользователя после авторизации")
    public void editUserWithLoginTest() {
        userClient.authUser(UserAuthData.withCorrectData(user));
        userClient.editUser(newUser, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.email", equalTo(newUser.getEmail().toLowerCase(Locale.ROOT)))
                .and()
                .body("user.name", equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Редактирование неавторизованного пользователя")
    public void editUserWithoutLoginTest() {
        userClient.editUserWithoutAuth(newUser)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", is(false));
    }

}