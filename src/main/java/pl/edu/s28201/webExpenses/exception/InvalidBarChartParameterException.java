package pl.edu.s28201.webExpenses.exception;

public class InvalidBarChartParameterException extends RuntimeException{
    public InvalidBarChartParameterException() {
        super();
    }

    public InvalidBarChartParameterException(String message) {
        super(message);
    }
}
