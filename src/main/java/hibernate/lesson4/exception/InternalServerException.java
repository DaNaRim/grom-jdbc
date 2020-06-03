package hibernate.lesson4.exception;

public class InternalServerException extends Exception {

    public InternalServerException(String message) {
        super(message);
    }
}