package be.vinci.pae.utils;

import be.vinci.pae.utils.exception.BizzException;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import be.vinci.pae.utils.exception.ReasonForConnectionRefusalException;
import be.vinci.pae.utils.exception.UserInvalidException;
import be.vinci.pae.utils.exception.UserOnHoldException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;

public class WebExceptionMapper implements ExceptionMapper<Throwable> {


  private static Response mapper(Throwable exception) {
    if (exception instanceof UserInvalidException) {
      return Response.status(Status.NOT_FOUND)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof ReasonForConnectionRefusalException
        | exception instanceof UserOnHoldException) {
      return Response.status(Status.FORBIDDEN)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof BizzException) {
      return Response.status(Status.UNAUTHORIZED)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof FatalException) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof ConflictException) {
      return Response.status(Status.CONFLICT)
          .entity(exception.getMessage())
          .build();
    }
    return Response.status(Status.NOT_FOUND)
        .entity(exception.getMessage())
        .build();
  }

  @Override
  public Response toResponse(Throwable exception) {
    MyLogger.writeError(Level.SEVERE, exception);
    return mapper(exception);
  }
}
