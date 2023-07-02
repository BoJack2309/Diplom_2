package users;

import org.apache.commons.lang3.RandomStringUtils;

public class UserRandomizer {

    public static User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public static User getUserWithoutEmail() {
        String email = "";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public static User getUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
        String password = "";
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public static User getUserWithoutName() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = "";
        return new User(email, password, name);
    }
}