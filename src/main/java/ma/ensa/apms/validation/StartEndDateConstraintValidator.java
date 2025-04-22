package ma.ensa.apms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartEndDateConstraintValidator implements ConstraintValidator<StartEndDateValidator, DateRangeHolder> {

    @Override
    public boolean isValid(DateRangeHolder entity, ConstraintValidatorContext context) {
        if (entity.getStartDate() == null || entity.getEndDate() == null) {
            return true;
        }

        boolean valid = true;

        if (!entity.getEndDate().isAfter(entity.getStartDate())) {
            valid = false;
        }

        return valid;
    }
}