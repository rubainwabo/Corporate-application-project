package utils;

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
   * permet de charger le fichier passé en paramètre.
   *
   * @param file le fichier à charger
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
   * permet de recupérer la valeur d'une clé contenu dans un fichier properties.
   *
   * @param key la clé associé à ce qu'on recherche
   * @return la valeur associé à la key
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * permet de recupérer la valeur d'une clé contenu dans un fichier properties.
   *
   * @param key la clé associé à ce qu'on recherche
   * @return la valeur associé à la key en int
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * permet de recupérer la valeur d'une clé contenu dans un fichier properties.
   *
   * @param key la clé associé à ce qu'on recherche
   * @return la valeur associé à la key en boolean
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }
}
