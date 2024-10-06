package ar.edu.uade;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface BrokerInterface {
    public Connection startConnection() throws Exception;
    void endConnection(Connection connection);
}
