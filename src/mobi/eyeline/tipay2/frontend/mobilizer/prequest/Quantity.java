package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boris on 23/03/17.
 */
public class Quantity {

    @JsonProperty(value = "unit")
    String unit;

    @JsonProperty(value = "value")
    String value;

}

