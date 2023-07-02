package clients;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import io.restassured.builder.RequestSpecBuilder;

public class Client {
    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}