package ma.ensa.apms.validation;

import java.time.LocalDateTime;

/**
 * Interface for entities with start and end dates that can be validated
 * by the StartEndDateConstraintValidator
 */
public interface DateRangeHolder {
    LocalDateTime getStartDate();

    LocalDateTime getEndDate();
}
