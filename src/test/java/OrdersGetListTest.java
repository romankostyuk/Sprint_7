import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;


public class OrdersGetListTest {
    private OrderClient orderClient;
    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }

    @Test
    public void getOrdersListTest(){
        ValidatableResponse ordersResponse = orderClient.getOrders();
        ordersResponse.assertThat().statusCode(200);
        ordersResponse.assertThat().body("orders", not(empty()));
    }
}
