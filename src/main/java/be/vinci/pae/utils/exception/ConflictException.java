package be.vinci.pae.utils.exception;

public class ConflictException extends RuntimeException {

  Exception exception;

  public ConflictException(String message) {
    super(message);
    this.exception = null;
  }

  public ConflictException(Exception exception) {
    super(exception.getMessage());
    this.exception = exception;
  }

  public Exception getException() {
    return this.exception;
  }
}
