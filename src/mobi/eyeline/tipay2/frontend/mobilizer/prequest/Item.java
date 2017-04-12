package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boris on 23/03/17.
 */
public class Item {

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "article")
    String article;

    @JsonProperty(value = "price")
    Amount price;

    @JsonProperty(value = "qti")
    Quantity qti;

}

