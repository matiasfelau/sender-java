package ar.edu.uade;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Broker implements BrokerInterface{
    private String host;
    private int port;
    private String username;
    private String password;

    public Broker(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Inicia una conexión con el Core.
     * @return La conexión con el Core.
     */
    public Connection startConnection() throws Exception {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            return factory.newConnection();
        } catch (Exception e) {
            throw new Exception("Error in CoreSender.Consumer.consume(): " + e.getMessage());
        }
    }

    /**
     * Cierra una conexión ABIERTA con el Core.
     * Se recomienda abrir conexiones distintas para publish() y consume() y cerrar la conexión utilizada para publish()
     * una vez enviado el mensaje.
     * @param connection Conexión con el Core que se obtiene de Broker.startConnection().
     */
    public void endConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("Error in CoreSender.Broker.endConnection(): " + e.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
