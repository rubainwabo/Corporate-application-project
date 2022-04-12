package utils;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import utils.exception.BizzException;
import utils.exception.FatalException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserInvalidException;
import utils.exception.UserOnHoldException;

public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  private static void logWriter(FileWriter f, StackTraceElement[] stackTrace)
      throws IOException {
    for (StackTraceElement s : stackTrace) {
      f.write(s + "\n");
    }
    f.write("\n");
  }

  private static String dateTime() {
    ZonedDateTime zonedDateTime =
        ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.of("Europe/Paris"));
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(zonedDateTime);
  }

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
    return Response.status(Status.NOT_FOUND)
        .entity(exception.getMessage())
        .build();
  }

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();
    try {
      String dir = "log" + File.separator;
      String filename = "log.txt";
      String absolutePath = dir + filename;
      File logger = new File(absolutePath);
      if (logger.createNewFile()) {
        System.out.println("File created : " + logger.getName());
      } else {
        System.out.println("File " + logger.getName() + " already exists");
      }
      FileWriter loggerWriter = new FileWriter(absolutePath, true);
      loggerWriter.write(
          dateTime() + "\nMessage d'erreur : "
              + exception.getMessage() + "\nStackTrace : \n");

      // Logs come from the point before the exception got fired
      if (exception instanceof FatalException) {
        FatalException e = (FatalException) exception;
        if (e.getException() != null) {
          logWriter(loggerWriter, e.getException().getStackTrace());
        }
      }
      if (exception instanceof BizzException) {
        BizzException e = (BizzException) exception;
        if (e.getException() != null) {
          logWriter(loggerWriter, e.getException().getStackTrace());
        }
      }
      // Logs come from the point where the exception got fired
      logWriter(loggerWriter, exception.getStackTrace());
      loggerWriter.close();
    } catch (IOException e) {
      System.out.println("An error occured while creating the logger");
      e.printStackTrace();
    }
    return mapper(exception);
  }

}