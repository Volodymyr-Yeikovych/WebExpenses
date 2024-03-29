package pl.edu.s28201.webExpenses.validation.time;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
@Documented
public @interface DateTime {

    String message() default "Invalid Date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
