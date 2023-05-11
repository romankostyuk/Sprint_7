import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierAuthorizationTest {
    private CourierClient courierClient;
    private int courierId;
    private Courier courier;
    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = Courier.builder()
                .login("roma32244")
                .password("roma3222")
                .firstName("Roman")
                .build();
        courierClient.create(courier);
    }
    /*+;
    +для авторизации нужно передать все обязательные поля;
    система вернёт ошибку, если неправильно указать логин или пароль;
    если какого-то поля нет, запрос возвращает ошибку;
    если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    +.*/
    @Test
    public void courierCanAuthorize() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int statusCode = loginResponse.extract().statusCode();
        //курьер может авторизоваться
        assertEquals(200, statusCode);

        courierId = loginResponse.extract().path("id");
        //успешный запрос возвращает id
        assertNotNull(courierId);
    }
    @After
    public void cleanUp(){courierClient.delete(courierId);}

}
