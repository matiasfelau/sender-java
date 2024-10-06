package ar.edu.uade;

import com.google.gson.Gson;
import com.rabbitmq.client.Delivery;

import java.nio.charset.StandardCharsets;

public class Utilities {

    /**
     * Convierte el Delivery que llega en un callback() a un Body.
     * @param delivery Mensaje que llega a través del consumer.
     * @see Body
     */
    public static Body convertDelivery(Delivery delivery) {
        String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(body, Body.class);
    }

    /**
     * Convierte un Body a cualquier clase del modelo.
     * @param body Cuerpo del mensaje obtenido con convertDelivery().
     * @param target Clase objetivo a la que se quieren convertir los datos del body.
     * @see Body
     */
    public static <T> T convertBody(Body body, Class<T> target) {
        Gson gson = new Gson();
        return gson.fromJson(body.getPayload(), target);
    }
}
