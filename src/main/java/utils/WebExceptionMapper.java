package utils;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import utils.exception.BizzException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserInvalidException;
import utils.exception.UserOnHoldException;

public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {

    try {
      File logger = new File("logger.txt");
      if (logger.createNewFile()) {
        System.out.println("Fichier créé: " + logger.getName());
      } else {
        System.out.println("le fichier logger.txt existe déjà");
      }
      FileWriter loggerWriter = new FileWriter("logger.txt");
      loggerWriter.write("Message d'erreur : "
          + exception.getMessage() + "\n\nStackTrace : \n");

      for (StackTraceElement s : exception.getStackTrace()) {
        loggerWriter.write(s + "\n");
      }

      loggerWriter.close();
    } catch (IOException e) {
      System.out.println("Un problème est survénu lors de la création du logger");
      e.printStackTrace();
    }
    exception.printStackTrace();
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
    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
