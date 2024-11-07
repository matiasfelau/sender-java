package ar.edu.uade;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Authenticator {
    private final String module;

    public Authenticator(Modules module) {
        this.module = String.valueOf(module).toLowerCase();
    }

    public String authenticate(Connection connection, String message) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Channel channel = connection.createChannel();

        String replyQueueName = module + ".rpc";

        final String corrId = UUID.randomUUID().toString();

        channel.queueDeclare(replyQueueName, false, true, false, null);

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", "gestion_interna.rpc", props, message.getBytes("UTF-8"));

        final CompletableFuture<String> response = new CompletableFuture<>();

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.get(1000, TimeUnit.MILLISECONDS);
        channel.basicCancel(ctag);
        return result;
    }
}
