package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boris on 23/03/17.
 */
public class Order {

    @JsonProperty(value = "id")
    String id;

    @JsonProperty(value = "amount")
    Amount amount;

    @JsonProperty(value = "total")
    Amount total;

    @JsonProperty(value = "items")
    Item[] items;

    @JsonProperty(value = "assignment")
    String assignment;




}
