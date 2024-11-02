package ar.edu.uade;

import com.google.gson.Gson;
import com.rabbitmq.client.Delivery;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Utilities {

    /**
     * Convierte el Delivery que llega en un callback() a un Body.
     * @param delivery Mensaje que llega a través del consumer.
     * @see Body
     * @throws ConverterException En caso de no haber provisto un Delivery válido.
     */
    public static Body convertDelivery(Delivery delivery) throws ConverterException {
        try {
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            return gson.fromJson(body, Body.class);
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertBody(): " + e.getMessage());
            throw new ConverterException("No se recibió un delivery.");
        }
    }

    /**
     * Convierte un Body a cualquier clase del modelo.
     * @param body Cuerpo del mensaje obtenido con convertDelivery().
     * @param target Clase objetivo a la que se quieren convertir los datos del body.
     * @see Body
     * @throws ConverterException En caso de no haber recibido un mensaje que se adapte correctamente a la
     * clase objetivo.
     */
    public static <T> T convertBody(Body body, Class<T> target) throws ConverterException {
        try {
            Gson gson = new Gson();
            return gson.fromJson(body.getPayload(), target);
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertBody(): " + e.getMessage());
            throw new ConverterException("No se pudo convertir a la clase objetivo.");
        }
    }

    /**
     * Convierte una clase a un formato de JSON String para poder ser enviada a otro módulo.
     * @param src Recibe cualquier objeto para convertirlo a un JSON String.
     * @return Devuelve un JSON String que representa al objeto.
     * @param <T> .
     * @throws ConverterException En caso de no haber podido convertir la clase.
     */
    public static <T> String convertClass(T src) throws ConverterException {
        try {
            Gson gson = new Gson();
            return gson.toJson(src);
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertClass(): " + e.getMessage());
            throw new ConverterException("No se pudo convertir la clase.");
        }
    }

    /**
     * Convierte un Array de String a un único String que pueda ser enviado a otro módulo.
     * @param mensajes Array contenedor de varios String.
     * @return Todos los elementos del Array concatenados en un único String.
     * @throws ConverterException En caso de no haber podido convertir todos los elementos del Array.
     * Podría pasar si algún elemento del Array es una clase que no fue convertida a JSON String previamente.
     */
    public static String convertArray(List<String> mensajes) throws ConverterException {
        try {
            return String.join("--!--##-->>DELIMITER<<--##--!--", mensajes);
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertArray(): " + e.getMessage());
            throw new ConverterException("No se pudo convertir el Array.");
        }
    }

    /**
     * Convierte un mensaje de Type Array a un Array contenedor de todos los elementos concatenados en el
     * mensaje.
     * @param mensaje Mensaje recibido de Type Array.
     * @return ArrayList con todos los elementos concatenados en el mensaje.
     * @throws ConverterException En caso de no haber recibido un Array adecuado en el mensaje.
     */
    public static List<String> convertString(String mensaje) throws ConverterException {
        try {
            if (!mensaje.contains("--!--##-->>DELIMITER<<--##--!--")) {
                throw new Exception();
            }
            return Arrays.asList(mensaje.split("--!--##-->>DELIMITER<<--##--!--"));
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertString(): " + e.getMessage());
            throw new ConverterException("No se pudo convertir el mensaje.");

        }
    }

    /**
     *
     * @param data Atributos y valores de la respectiva clase.
     * @param target Clase objetivo.
     * @return Devuelve un objeto de la clase objetivo, construida con la información de data
     * @param <T> .
     * @throws ConverterException Puede ocurrir si la data recibida no coincide ningún constructor de la clase objetivo.
     */
    public static <T> T convertElement(String data, Class<T> target) throws ConverterException {
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, target);
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Utilities.convertElement(): " + e.getMessage());
            throw new ConverterException("No se pudo convertir el elemento.");
        }
    }
}
