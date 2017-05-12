package mobi.eyeline.tipay2.frontend.mobilizer.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * Created by gev on 11.05.17.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect( fieldVisibility = NONE, getterVisibility = NONE, setterVisibility = NONE, isGetterVisibility = NONE, creatorVisibility = NONE)
public class Amount {

  @JsonProperty(required = true)
  protected int amount;

  public static Amount create(int amount) {
    Amount a = new Amount();
    a.amount = amount;
    return a;
  }
}
