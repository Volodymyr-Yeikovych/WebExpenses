package pl.edu.s28201.webExpenses.exception;

public class AppUserAlreadyExistsException extends Exception {
    public AppUserAlreadyExistsException(String message) {
        super(message);
    }
}
