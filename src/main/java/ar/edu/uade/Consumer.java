package ar.edu.uade;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Objeto que inicia un hilo consumidor
 * Ejemplo de uso:
 * <pre>
 *     Consumer consumer = new Consumer(new CallbackInterface() {
 *             Override
 *             public void callback(String consumerTag, Delivery delivery) {
 *                 Body body = convertDelivery(delivery);
 *                 convertBody(body, Usuario.class);
 *             }
 *         });
 * </pre>
 */
public class Consumer implements ConsumerInterface {
    private CallbackInterface callback;

    public Consumer(CallbackInterface callback) {
        this.callback = callback;
    }

    /**
     * Inicia un hilo consumidor que procesa los mensajes recibidos siguiendo el algoritmo definido en la función
     * callback().
     * @param connection Conexión con el Core obtenida con la función Broker.startConnection().
     * @param origin Módulo desde el que se consumen mensajes. Debe ser un módulo válido perteneciente al enum Modules.
     * @see Modules
     */
    public void consume(Connection connection, Modules origin) {
        try {
            String realOrigin = String.valueOf(origin).toLowerCase();
            Channel channel = connection.createChannel();

            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", realOrigin + ".trapping");
            arguments.put("x-dead-letter-routing-key", realOrigin + ".trapping");

            channel.queueDeclare(realOrigin, true, false, false, arguments);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                callback.callback(consumerTag, delivery);
            };

            channel.basicConsume(realOrigin, true, deliverCallback, consumerTag -> {});

            System.out.println(String.format("Esperando mensajes de %s", realOrigin));

        } catch (Exception e) {
            System.out.println("Error in CoreSender.Consumer.consume(): " + e.getMessage());
        }
    }
}
