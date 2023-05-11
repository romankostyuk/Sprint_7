import io.restassured.response.ValidatableResponse;
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
    private static final String LOGIN = "TestLogin5612";
    private static final String PASSWORD = "!Pass56432";
    public CourierCannotBeAuthorizedTest(CourierCredentials courierCredentials, int statusCode) {
        this.courierCredentials = courierCredentials;
        this.expectedStatusCode = statusCode;
    }
    @Parameterized.Parameters(name = "{0} - {1}")
    public static Object[][] getTestData(){
        return new Object[][]{
                //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
                {CourierCredentials.builder().login("NotExistingUserLogin123321").password("SomePassword").build(), 404},
                //система вернёт ошибку, если неправильно указать логин или пароль;
                {CourierCredentials.builder().login(LOGIN).password("WrongPassword").build(), 404},
                //если какого-то поля нет, запрос возвращает ошибку;
                {CourierCredentials.builder().login(LOGIN).build(), 400},
                {CourierCredentials.builder().password(PASSWORD).build(), 400},
                //для авторизации нужно передать все обязательные поля;
                {CourierCredentials.builder().login(LOGIN).password(PASSWORD).build(), 200},
        };
    }
    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = Courier.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }
    @Test
    public void CourierCannotBeAuthorizedTest(){
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int actualStatusCode = loginResponse.extract().statusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }
    @After
    public void cleanUp() {courierClient.delete(courierId);}
}
