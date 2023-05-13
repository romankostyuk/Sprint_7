import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierCannotBeCreatedTest {
    private CourierClient courierClient;
    private int courierId;
    private final Courier courier;
    private final int expectedStatusCode;
    private int actualStatusCode;
    private static final String login = RandomStringUtils.randomAlphabetic(6);
    private static final String password = RandomStringUtils.randomAlphabetic(8);
    public CourierCannotBeCreatedTest(Courier courier, int statusCode) {
        this.courier = courier;
        this.expectedStatusCode = statusCode;
    }
    @Parameterized.Parameters(name = "{0} - {1}")
    public static Object[][] getTestData(){
        return new Object[][]{
                {Courier.builder().login(login).build(), 400},
                {Courier.builder().password(password).build(), 400},
                {Courier.builder().login(login).password(password).build(), 201},
        };
    }
    @Before
    public void setUp(){
        courierClient = new CourierClient();
    }
    @Test
    public void courierCannotBeCreatedWithoutAnyRequiredField() {
        ValidatableResponse createResponse = courierClient.create(courier);

        actualStatusCode = createResponse.extract().statusCode();
        //чтобы создать курьера, нужно передать в ручку все обязательные поля;
        //если одного из полей нет, запрос возвращает ошибку;
        assertEquals(expectedStatusCode, actualStatusCode);
    }
    @After
    public void cleanUp(){
        if (actualStatusCode == 201) {
            ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
            courierId = loginResponse.extract().path("id");
            courierClient.delete(courierId);
        }
    }
}
