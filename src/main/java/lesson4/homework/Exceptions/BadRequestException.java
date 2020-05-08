package lesson4.homework.Exceptions;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }
}