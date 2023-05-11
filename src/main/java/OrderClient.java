import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    private static final String ORDER_CREATE_PATH = "/api/v1/orders";
    private static final String ORDER_CANCEL_PATH = "/api/v1/orders/cancel";

    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpecification())
                .body(order)
                .when()
                .post(ORDER_CREATE_PATH)
                .then();
    }
    public ValidatableResponse cancel(int track) {
        return given()
                .spec(getSpecification())
                .body("{\"track\":"+ track +"}")
                .when()
                .put(ORDER_CANCEL_PATH)
                .then();
    }
    public ValidatableResponse getOrders() {
        return  given()
                .spec(getSpecification())
                .when()
                .get(ORDER_CREATE_PATH)
                .then();
    }

}
