package com.viniciuscampitelli;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessage {
    private final Map<String, Object> notification = new HashMap<>();
    private final Map<String, Object> data = new HashMap<>();

    public FirebaseMessage withTitle(String title) {
        this.notification.put("title", title);
        return this;
    }

    public FirebaseMessage withBody(String body) {
        this.notification.put("body", body);
        return this;
    }

    public FirebaseMessage withDataEntry(String key, String value) {
        this.data.put(key, value);
        return this;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
