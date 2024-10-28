package ar.edu.uade;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


public class Publisher implements PublisherInterface{
    private Modules origin;

    public Publisher(Modules origin) {
        this.origin = origin;
    }

    /**
     * Envía un mensaje o una clase pasada a través de un toString() a cualquier módulo del enum Modules.
     * @param connection Conexión con el Core obtenida con la función Broker.startConnection().
     * @param message String que se enviará al módulo de destino.
     * @param destination Módulo destino al que se enviará el mensaje. Debe ser un módulo válido del enum Modules.
     * @param use_case Caso de uso que generó el mensaje.
     * @param token JWT
     * @param type Tipo del dato que se envio (String, JSON o Array)
     * @param target La clase de objetos que se contienen dentro de un Array si corresponde. De otra forma, puede ser nulo o vacío.
     * @see Modules
     */
    public void publish(Connection connection, String message, Modules destination, String use_case, String token, Types type, String target) {
        try {
            String realDestination = String.valueOf(destination).toLowerCase();
            String realType = String.valueOf(type).toLowerCase();
            String realOrigin = String.valueOf(origin).toLowerCase();
            Gson gson = new Gson();
            Body payload = new Body(realOrigin, realDestination, use_case, message, "0", token, realType, target);
            String jsonString = gson.toJson(payload);
            Channel channel = connection.createChannel();
            channel.queueDeclare("core", true, false, false, null);
            channel.basicPublish("core", "core", null, jsonString.getBytes());
            System.out.println(String.format("Mensaje enviado con éxito al módulo %s para el caso de uso %s", destination, use_case));
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Publisher.publish(): " + e.getMessage());
        }
    }

    public Modules getOrigin() {
        return origin;
    }

    public void setOrigin(Modules origin) {
        this.origin = origin;
    }
}
