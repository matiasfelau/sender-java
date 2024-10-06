package ar.edu.uade;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


public class Publisher implements PublisherInterface{
    private String origin;

    public Publisher(String origin) {
        this.origin = origin.toLowerCase();
    }

    /**
     * Envía un mensaje o una clase pasada a través de un toString() a cualquier módulo del enum Modules.
     * @param connection Conexion con el Core obtenida con la función Broker.startConnection().
     * @param message String que se enviará al módulo de destino.
     * @param destination Módulo destino al que se enviará el mensaje. Debe ser un módulo válido del enum Modules.
     * @param use_case Caso de uso que generó el mensaje.
     * @see Modules
     */
    public void publish(Connection connection, String message, Modules destination, String use_case) {
        try {
            String realDestination = String.valueOf(destination).toLowerCase();
            Gson gson = new Gson();
            Body payload = new Body(origin, realDestination, use_case, message, "0");
            String jsonString = gson.toJson(payload);
            Channel channel = connection.createChannel();
            channel.queueDeclare("core", true, false, false, null);
            channel.basicPublish("core", "core", null, jsonString.getBytes());
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Publisher.publish(): " + e.getMessage());
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin.toLowerCase();
    }
}
