package ar.edu.uade;

/**
 * Objeto que contiene los datos de un mensaje consumido.
 * <pre>
 *     origin: Módulo de origen del mensaje.
 *     destination: Módulo de destino del mensaje.
 *     useCase: Caso de uso que generó al mensaje.
 *     payload: Cuerpo del mensaje.
 *     status: Estado del mensaje.
 * </pre>
 *
 */
public class Body {
    private String origin;
    private String destination;
    private String useCase;
    private String payload;
    private String status;
    private String token;
    private String type;

    public Body(String origin, String destination, String useCase, String payload, String status, String token, String type) {
        this.origin = origin;
        this.destination = destination;
        this.useCase = useCase;
        this.payload = payload;
        this.status = status;
        this.token = token;
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
