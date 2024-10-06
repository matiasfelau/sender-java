package ar.edu.uade;

import com.rabbitmq.client.Delivery;

public interface CallbackInterface {
    void callback(String consumerTag, Delivery delivery);
}
