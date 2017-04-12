package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boris on 23/03/17.
 */
public class Subject {

    @JsonProperty(value = "uid")
    String uid;

    @JsonProperty(value = "mobile")
    String mobile;

    @JsonProperty(value = "email")
    String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
