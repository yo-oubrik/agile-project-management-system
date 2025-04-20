package ma.ensa.apms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ma.ensa.apms.modal.Project;

public class ProjectDatesValidator implements ConstraintValidator<ValidProjectDates, Project> {

    @Override
    public boolean isValid(Project project, ConstraintValidatorContext context) {
        if (project.getStartDate() == null || project.getEndDate() == null) {
            return true;
        }

        boolean valid = true;

        // Check endDate is after startDate
        if (!project.getEndDate().isAfter(project.getStartDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date must be after start date")
                   .addPropertyNode("endDate").addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}