import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierCannotBeAuthorizedTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private final CourierCredentials courierCredentials;
    private final int expectedStatusCode;
    private static final String login = RandomStringUtils.randomAlphabetic(6);
    private static final String password = RandomStringUtils.randomAlphabetic(8);
    public CourierCannotBeAuthorizedTest(CourierCredentials courierCredentials, int statusCode) {
        this.courierCredentials = courierCredentials;
        this.expectedStatusCode = statusCode;
    }
    @Parameterized.Parameters(name = "{0} - {1}")
    public static Object[][] getTestData(){
        return new Object[][]{
                //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
                {CourierCredentials.builder().login(RandomStringUtils.randomAlphabetic(6)).password(RandomStringUtils.randomAlphabetic(8)).build(), 404},
                //система вернёт ошибку, если неправильно указать логин или пароль;
                {CourierCredentials.builder().login(login).password(RandomStringUtils.randomAlphabetic(8)).build(), 404},
                //если какого-то поля нет, запрос возвращает ошибку;
                {CourierCredentials.builder().login(login).build(), 400},
                {CourierCredentials.builder().password(password).build(), 400},
                //для авторизации нужно передать все обязательные поля;
                {CourierCredentials.builder().login(login).password(password).build(), 200},
        };
    }
    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = Courier.builder()
                .login(login)
                .password(password)
                .build();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }
    @Test
    public void courierCannotBeAuthorizedTest(){
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int actualStatusCode = loginResponse.extract().statusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }
    @After
    public void cleanUp() {courierClient.delete(courierId);}
}
