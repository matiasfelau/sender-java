package ar.edu.uade;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public interface ConsumerInterface {
    void consume (Connection connection, Modules origin);
}
