import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;
    private Courier courier;
    @Before
    public void setUp(){
        courierClient = new CourierClient();
        courier = Courier.builder()
                .login("roma32222")
                .password("roma3222")
                .firstName("Roman")
                .build();
    }
    @Test
    public void courierCanBeCreated(){
        ValidatableResponse createResponse = courierClient.create(courier);

        boolean isCreated = createResponse.extract().path("ok");
        //успешный запрос возвращает ok: true;
        assertTrue(isCreated);

        int statusCode = createResponse.extract().statusCode();
        //запрос возвращает правильный код ответа;
        assertEquals(201, statusCode);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        //курьера можно создать;
        assertNotNull(courierId);
    }
    @Test
    public void sameCouriersCannotBeCreated(){
        courierClient.create(courier);
        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        //нельзя создать двух одинаковых курьеров;
        //если создать пользователя с логином, который уже есть, возвращается ошибка.
        assertEquals(409, statusCode);
    }

    @After
    public void cleanUp(){courierClient.delete(courierId);}
}
