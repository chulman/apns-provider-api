package com.chulm.apns.format.payload;

import org.json.JSONObject;

public class Payload {


    private String title;
    private String body;

    private int badge;
    private String sound;
    private int content_available;
    private String category;

    private String action_loc_key;
    private String loc_key;
    private String[] loc_args;

    private String launch_image;


    private JSONObject data = null;
    private JSONObject aps = null;
    private JSONObject alert = null;

    public Payload() {
        data = new JSONObject();
        aps = new JSONObject();
        alert = new JSONObject();

    }

    public void append(String key, Object value){
        data.append(key,value);
    }


    public String parseToJson() {
        aps.put(Key.ALERT,alert.toString());
        data.put(Key.APS,aps.toString());

        return data.toString();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        alert.put(Key.ALERT_TITLE,getTitle());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        alert.put(Key.ALERT_BODY,getTitle());
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
        aps.put(Key.BADGE, badge);
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
        aps.put(Key.SOUND,sound);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        aps.put(Key.CATEGORY,category);
    }

    public int getContent_available() {
        return content_available;
    }

    public void setContent_available(int content_available) {
        this.content_available = content_available;
        aps.put(Key.CONTENT_AVAILABLE,content_available);
    }

    public String getAction_loc_key() {
        return action_loc_key;
    }

    public void setAction_loc_key(String action_loc_key) {
        this.action_loc_key = action_loc_key;
        alert.put(Key.ALERT_ACTION_LOC_KEY,action_loc_key);
    }

    public String getLoc_key() {
        return loc_key;
    }

    public void setLoc_key(String loc_key) {
        this.loc_key = loc_key;
        alert.put(Key.ALERT_LOC_KEY, loc_key);
    }

    public String[] getLoc_args() {
        return loc_args;
    }

    public void setLoc_args(String[] loc_args) {
        this.loc_args = loc_args;
        alert.put(Key.ALERT_LOC_ARGS, loc_args);
    }

    public String getLaunch_image() {
        return launch_image;
    }

    public void setLaunch_image(String launch_image) {
        this.launch_image = launch_image;
        aps.put(Key.ALERT_LAUNCH_IMAGE,launch_image);
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getAps() {
        return aps;
    }

    public void setAps(JSONObject aps) {
        this.aps = aps;
    }

    public JSONObject getAlert() {
        return alert;
    }

    public void setAlert(JSONObject alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "data=" + data +
                ", aps=" + aps +
                ", alert=" + alert +
                '}';
    }
}
