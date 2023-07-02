import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserRandomizer;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private String userToken;

    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = UserRandomizer.getRandomUser();
        userClient = new UserClient();
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if(userToken != null) {
            userClient.deleteUser(userToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя с корректными данными")
    public void createUserTest() {
        userToken = userClient.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание дублирующего пользователя")
    public void createDuplicateUserTest() {

        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));

        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без указания email")
    public void createUserWithoutEmailTest() {
        user = UserRandomizer.getUserWithoutEmail();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания password")
    public void createUserWithoutPasswordTest() {
        user = UserRandomizer.getUserWithoutPassword();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    public void createUserWithoutNameTest() {
        user = UserRandomizer.getUserWithoutName();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}