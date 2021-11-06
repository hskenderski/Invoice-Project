package bg.codix.spring.invoice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{

  @ExceptionHandler(InvalidException.class)
  public ResponseEntity<String> handlerInvalidException(InvalidException ex)
  {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception)
  {
    StringBuilder message = new StringBuilder();
    Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
    for (ConstraintViolation<?> violation : violations) {
      message.append(violation.getMessage().concat("; "));
    }
    return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
  }
}
