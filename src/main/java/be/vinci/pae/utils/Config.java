package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static Properties props;

  /**
   * allows to load the file passed in parameter.
   *
   * @param file the file to be uploaded
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * allows to retrieve the value of a key contained in a properties file.
   *
   * @param key the key associated with what we are looking for
   * @return the value associated with the key
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * allows to retrieve the value of a key contained in a properties file.
   *
   * @param key the key associated with what we are looking for
   * @return the value associated with the key as int
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * allows to retrieve the value of a key contained in a properties file.
   *
   * @param key the key associated with what we are looking for
   * @return the value associated with the key as boolean
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }
}
