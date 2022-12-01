package com.viniciuscampitelli;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class DefaultSettings {
    private static AwsCredentialsProvider provider;
    private static Region region;

    public static AwsCredentialsProvider getCredentialsProvider() {
        if (provider == null) {
            provider = ProfileCredentialsProvider.create();
        }
        return provider;
    }

    public static Region getRegion() {
        if (region == null) {
            region = Region.US_EAST_1;
        }
        return region;
    }
}
