import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
        final String login = RandomStringUtils.randomAlphabetic(6);
        final String password = RandomStringUtils.randomAlphabetic(8);
        final String firstName = RandomStringUtils.randomAlphabetic(4);
        courier = Courier.builder()
                .login(login)
                .password(password)
                .firstName(firstName)
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
