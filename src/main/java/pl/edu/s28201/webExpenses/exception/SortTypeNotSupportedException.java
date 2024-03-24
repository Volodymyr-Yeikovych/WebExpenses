package pl.edu.s28201.webExpenses.exception;

public class SortTypeNotSupportedException extends RuntimeException {

    public SortTypeNotSupportedException(String message) {
        super(message);
    }
}
