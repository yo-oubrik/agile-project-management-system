package ma.ensa.apms.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProjectDatesValidator.class)
public @interface ValidProjectDates {
    String message() default "Invalid project end date , end date must be after start date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}