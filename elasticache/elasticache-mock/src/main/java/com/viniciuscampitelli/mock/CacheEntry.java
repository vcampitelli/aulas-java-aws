package com.viniciuscampitelli.mock;

import java.time.Instant;

public record CacheEntry(String value, Instant expiration) {
}
