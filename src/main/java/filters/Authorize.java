package filters;


import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.ws.rs.NameBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Authorize
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
}


