package pl.itacademy.schedule.exception;

@SuppressWarnings("serial")
public class IncorrectParametersException extends Exception {
    public IncorrectParametersException(String message) {
        super(message);
    }
}
