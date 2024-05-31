package pl.edu.s28201.webExpenses.validation.time;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class LocalDateValidator implements ConstraintValidator<Date, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.isBefore(LocalDate.now()) || value.isEqual(LocalDate.now());
    }
}
