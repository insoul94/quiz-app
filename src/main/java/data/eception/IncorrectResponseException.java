package data.eception;

public class IncorrectResponseException extends Exception{
    public IncorrectResponseException(String message) {
        super(message);
    }
}
