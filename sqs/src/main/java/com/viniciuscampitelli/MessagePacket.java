package com.viniciuscampitelli;

import java.util.Date;
import java.util.UUID;

public class MessagePacket {
    private final UUID id;
    private final String contents;
    private final Date date;

    public MessagePacket(String body, Date date, UUID id) {
        this.id = id;
        this.contents = body;
        this.date = date;
    }

    public MessagePacket(String body, Date date) {
        this.id = UUID.randomUUID();
        this.contents = body;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Date getDate() {
        return date;
    }
}
