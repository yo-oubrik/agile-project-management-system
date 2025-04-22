package ma.ensa.apms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method operations.
 * Apply this annotation to methods that should be logged.
 * 
 * Usage:
 * 
 * @LogOperation(description = "Create user story")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    String description() default "";
}
