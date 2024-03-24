package pl.edu.s28201.webExpenses.exception;

public class CurrencyNotSupportedException extends RuntimeException{

    public CurrencyNotSupportedException(String message) {
        super(message);
    }
}
