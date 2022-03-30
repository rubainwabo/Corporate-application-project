package utils.exception;

public class BizzException extends RuntimeException {

  Exception exception;

  public BizzException(String message) {
    super(message);
    this.exception = null;
  }

  public BizzException(Exception exception) {
    super(exception.getMessage());
    this.exception = exception;
  }

  public Exception getException() {
    return this.exception;
  }
}