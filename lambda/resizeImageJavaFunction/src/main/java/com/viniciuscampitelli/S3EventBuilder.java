package com.viniciuscampitelli;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import java.util.ArrayList;
import java.util.List;

public class S3EventBuilder {
    public static S3Event build(String bucketName, String filename) {
        S3EventNotification.UserIdentityEntity userIdentity = new S3EventNotification.UserIdentityEntity("example");
        S3EventNotification.S3EventNotificationRecord record = new S3EventNotification.S3EventNotificationRecord(
                "us-east-1",
                "ObjectCreated:Put",
                "aws:s3",
                "2022-11-30T21:00:00.000Z",
                "2.0",
                new S3EventNotification.RequestParametersEntity("127.0.0.1"),
                new S3EventNotification.ResponseElementsEntity(
                        "EXAMPLE123456789",
                        "EXAMPLE123/5678abcdefghijklambdaisawesome/mnopqrstuvwxyzABCDEFGH"
                ),
                new S3EventNotification.S3Entity(
                        "testConfigRule",
                        new S3EventNotification.S3BucketEntity(
                                bucketName,
                                userIdentity,
                                "arn:aws:s3:::" + bucketName
                        ),
                        new S3EventNotification.S3ObjectEntity(
                                filename,
                                1024L,
                                "0123456789abcdef0123456789abcdef",
                                "",
                                "0A1B2C3D4E5F678901"
                        ),
                        "1.0"
                ),
                userIdentity
        );

        List<S3EventNotification.S3EventNotificationRecord> records = new ArrayList<S3EventNotification.S3EventNotificationRecord>();
        records.add(record);
        return new S3Event(records);
    }
}
