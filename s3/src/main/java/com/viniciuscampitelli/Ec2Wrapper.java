package com.viniciuscampitelli;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

class Ec2Wrapper {

    public static void run() throws Ec2Exception {
        Ec2Client ec2Client = Ec2Client.builder()
                .region(DefaultSettings.getRegion())
                .credentialsProvider(DefaultSettings.getCredentialsProvider())
                .build();

        DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).build();
        DescribeInstancesResponse response = ec2Client.describeInstances(request);
        for (Reservation reservation : response.reservations()) {
            for (Instance instance : reservation.instances()) {
                System.out.println("Instance Id is " + instance.instanceId());
                System.out.println("Image id is " + instance.imageId());
                System.out.println("Instance type is " + instance.instanceType());
                System.out.println("Instance state name is " + instance.state().name());
                System.out.println("Monitoring information is " + instance.monitoring().state());
                System.out.println("");
            }
        }
        ec2Client.close();
    }
}
