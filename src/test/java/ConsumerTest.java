import ar.edu.uade.*;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class ConsumerTest {

    @Test
    public void testConsume() {

        Broker broker = new Broker("3.142.225.39", 5672, "usuario", "!9*@B3#^447@@65y5@@8");
        try {
            Connection consumerConnection = broker.startConnection();
            Consumer consumer = new Consumer(new CallbackInterface() {
                @Override
                public void callback(String consumerTag, Delivery delivery) {
                    /*
                    try {
                        Body body = Utilities.convertDelivery(delivery); //Convierto el Delivery recibido en el Callback a un Body.
                        //... (Otras comparaciones para los demás Type)
                        if (Objects.equals(body.getType(), Types.ARRAY.name())) { //Confirmo que el mensaje recibido contiene a un Array.
                            List<String> jsonArr = Utilities.convertString(body.getPayload()); //Obtengo el Array de JSON del body.
                            //... (Otras comparaciones para las demás clases esperadas)
                            if (Objects.equals(body.getTarget(), "Usuario")) { //Identifico la clase de objetos que están contenidos dentro del Array.
                                for (String json : jsonArr) { //Itero por cada elemento del Array de JSON.
                                    Usuario usuario = Utilities.convertElement(json, Usuario.class); //Convierto el JSON a un objeto del modelo.
                                }
                            }
                        }
                    } catch (ConverterException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Ocurrió un error al procesar el mensaje recibido: " + e.getMessage());
                    }

                     */
                }
            });
            consumer.consume(consumerConnection, Modules.USUARIO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

