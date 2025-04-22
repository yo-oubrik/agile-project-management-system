package ma.ensa.apms.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import ma.ensa.apms.annotation.LogOperation;
import ma.ensa.apms.logging.LoggerUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut that matches all service methods
     */
    @Pointcut("within(ma.ensa.apms.service..*)")
    public void servicePointcut() {
    }

    /**
     * Pointcut for methods annotated with @LogExecutionTime
     */
    @Pointcut("@annotation(ma.ensa.apms.annotation.LogExecutionTime)")
    public void logExecutionTimePointcut() {
    }

    /**
     * Pointcut for methods annotated with @LogOperation
     */
    @Pointcut("@annotation(ma.ensa.apms.annotation.LogOperation)")
    public void logOperationPointcut() {
    }

    /**
     * Log method execution time for methods annotated with @LogExecutionTime
     */
    @Around("logExecutionTimePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        LoggerUtils.logServiceInfo(String.format("Execution time of %s.%s: %d ms",
                className, methodName, stopWatch.getTotalTimeMillis()));

        return result;
    }

    /**
     * Log operations for methods annotated with @LogOperation
     */
    @Around("logOperationPointcut()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogOperation logOperation = method.getAnnotation(LogOperation.class);
        String description = logOperation.description();

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String args = Arrays.toString(joinPoint.getArgs());

        LoggerUtils.logServiceInfo(String.format("Executing operation: %s - %s.%s() with arguments: %s",
                description, className, methodName, args));

        Object result = joinPoint.proceed();

        LoggerUtils.logServiceInfo(String.format("Completed operation: %s - %s.%s()",
                description, className, methodName));

        return result;
    }

    /**
     * Log methods in service layer
     */
    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getSignature().getDeclaringType().toString().contains("$$")) {
            // Skip proxy classes
            return joinPoint.proceed();
        }

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        LoggerUtils.logServiceDebug(String.format("Entering: %s.%s()", className, methodName));

        Object result = joinPoint.proceed();

        LoggerUtils.logServiceDebug(String.format("Exiting: %s.%s()", className, methodName));

        return result;
    }

    /**
     * Log exceptions thrown from service layer
     */
    @AfterThrowing(pointcut = "servicePointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        LoggerUtils.logServiceError(String.format("Exception in %s.%s() with message: %s",
                className, methodName, exception.getMessage()), exception);
    }
}
