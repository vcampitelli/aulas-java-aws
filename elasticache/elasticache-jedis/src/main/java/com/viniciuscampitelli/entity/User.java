package com.viniciuscampitelli.entity;

import java.util.List;

public class User {
    private final Integer id;
    private final String name;
    private final String username;
    private final List<Address> addresses;

    public User(Integer id, String name, String username, List<Address> addresses) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.addresses = addresses;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
