package pl.edu.s28201.webExpenses.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.s28201.webExpenses.model.dto.AppUserDto;

public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, Object> {

    @Override
    public void initialize(MatchingPasswords constraint) {}

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        AppUserDto user = (AppUserDto) o;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}