import ar.edu.uade.Broker;
import ar.edu.uade.CallbackInterface;
import ar.edu.uade.Consumer;
import ar.edu.uade.Modules;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import org.junit.Test;

public class ConsumerTest {

    @Test
    public void testConsume() {

        Broker broker = new Broker("3.142.225.39", 5672, "admin", "59482*M97&!@3@%$2$r@");

        try {

            Connection consumerConnection = broker.startConnection();

            Consumer consumer = new Consumer(new CallbackInterface() {
                @Override
                public void callback(String consumerTag, Delivery delivery) {

                }
            });

            consumer.consume(consumerConnection, Modules.USUARIO);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }
}
