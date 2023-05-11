import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class OrderColorsTest {
    private OrderClient orderClient;
    private Order order;
    private int orderTrack;
    private int actualStatusCode;
    private final int expectedStatusCode;
    private final String[] colors;
    public OrderColorsTest(String[] colors, int expectedStatusCode){
        this.colors = colors;
        this.expectedStatusCode = expectedStatusCode;
    }
    @Parameterized.Parameters(name = "{0} - {1}")
    public static Object[][] getTestData(){
        return new Object[][]{
                {new String[] {"Black"}, 201},
                {new String[] {"Gray"}, 201},
                {new String[] {"Black", "Gray"}, 201},
                {new String[] {""}, 201},
        };
    }
    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }
    @Test
    public void OrderColorsTest() {
        order = Order.
                builder().
                firstName("Роман").lastName("Костюк").
                address("Москва").metroStation("Свиблово").
                phone("+79778294218").rentTime(1).
                deliveryDate("15.02.2023").comment("Привет").
                color(colors).build();
        ValidatableResponse createResponse = orderClient.create(order);
        actualStatusCode = createResponse.extract().statusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
        orderTrack = createResponse.extract().path("track");
        assertNotNull(orderTrack);
    }
    @After
    public void cleanUp(){
        if (actualStatusCode == 201) {
            orderClient.cancel(orderTrack);
        }
    }
}
