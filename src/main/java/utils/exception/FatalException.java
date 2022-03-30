package utils.exception;

public class FatalException extends RuntimeException {

  Exception exception;

  public FatalException(String message) {
    super(message);
    this.exception = null;
  }

  public FatalException(Exception exception) {
    super(exception.getMessage());
    this.exception = exception;
  }

  public Exception getException() {
    return this.exception;
  }
}