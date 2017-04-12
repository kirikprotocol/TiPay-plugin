package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boris on 23/03/17.
 */
public class Amount {

    @JsonProperty(value = "kind")
    String kind;

    @JsonProperty(value = "unit")
    String unit;

    @JsonProperty(value = "precision")
    int precision;

    @JsonProperty(value = "value")
    double value;

    @JsonProperty(value = "fee")
    Fee fee;

    @JsonProperty(value = "tax")
    Fee tax;

    @JsonProperty(value = "total")
    double total;

}
