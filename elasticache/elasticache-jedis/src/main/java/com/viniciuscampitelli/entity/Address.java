package com.viniciuscampitelli.entity;

public class Address {
    private final String street;
    private final String number;

    public Address(String street, String number) {
        this.street = street;
        this.number = number;
    }

    public Address(String street, Integer number) {
        this(street, String.valueOf(number));
    }
}
