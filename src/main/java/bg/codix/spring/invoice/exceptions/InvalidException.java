package bg.codix.spring.invoice.exceptions;

public class InvalidException extends RuntimeException
{

  public InvalidException(String message)
  {
    super(message);
  }
}
