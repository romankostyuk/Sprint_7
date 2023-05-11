import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
public class CourierClient extends RestClient{
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpecification())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getSpecification())
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(int id) {
        return given()
                .spec(getSpecification())
                .when()
                .delete(COURIER_PATH + "/" + id)
                .then();
    }
}
