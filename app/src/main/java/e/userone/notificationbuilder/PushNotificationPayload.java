package e.userone.notificationbuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by userone on 2/27/2018.
 */

public class PushNotificationPayload {
    @SerializedName("acme-type")
    @Expose
    private String acme_type;
    @SerializedName("acme-id")
    @Expose
    private String acme_id;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("title")
    @Expose
    private String tittle;

    @SerializedName("message")
    @Expose
    private String message;

    private Boolean gender;
    private String timeZone;

    private int notificationId;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAcme_type() {
        return acme_type;
    }

    public void setAcme_type(String acme_type) {
        this.acme_type = acme_type;
    }

    public String getAcme_id() {
        return acme_id;
    }

    public void setAcme_id(String acme_id) {
        this.acme_id = acme_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
