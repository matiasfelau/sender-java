package ar.edu.uade;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public interface PublisherInterface {
    void publish(Connection connection, String message, Modules destination, String use_case, String token, Types type, String target);
}
