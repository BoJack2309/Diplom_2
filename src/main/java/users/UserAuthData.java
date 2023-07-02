package users;

public class UserAuthData {

    private String email;
    private String password;

    public UserAuthData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserAuthData withCorrectData(User user) {
        return new UserAuthData(user.getEmail(), user.getPassword());
    }

    public static UserAuthData withIncorrectEmail(User user) {
        return new UserAuthData(user.getEmail() + "errorData", user.getPassword());
    }

    public static UserAuthData withIncorrectPassword(User user) {
        return new UserAuthData(user.getEmail(), user.getPassword() + "errorData");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = password;
    }

}