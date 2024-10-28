package ar.edu.uade;

public class ConverterException extends Exception {

    public ConverterException(String mensaje) {
        super(mensaje);
    }

    public ConverterException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
