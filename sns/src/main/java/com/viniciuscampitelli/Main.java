package com.viniciuscampitelli;

import com.google.gson.Gson;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Region region = Region.US_EAST_1;

        try {
            SnsClient sns = SnsClient.builder()
                    .region(region)
                    .credentialsProvider(
                            ProfileCredentialsProvider.create()
                    )
                    .build();

            String message = customFirebaseMessage();
            System.out.println(message);

            String targetArn = ""; // @TODO coloque aqui o ARN do endpoint do tópico SNS
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .targetArn(targetArn)
                    .messageStructure("json")
                    .build();

            PublishResponse result = sns.publish(request);

            System.out.println(
                    "Mensagem enviada: " + result.messageId()
            );
        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * @link https://www.agalera.eu/sns-firebase-android-ios/
     */
    private static String customFirebaseMessage() {
        Map<String, String> customMessage = new HashMap<>();
        final String FIREBASE_PROTOCOL = "GCM";
        customMessage.put(FIREBASE_PROTOCOL, getFirebaseMessage());
        return new Gson().toJson(customMessage);
    }

    private static String getFirebaseMessage() {
        FirebaseMessage message = new FirebaseMessage()
                .withTitle("Conta aprovada")
                .withBody("Sua conta foi aprovada")
                .withDataEntry("status", "APROVADO")
                .withDataEntry("agencia", "1234")
                .withDataEntry("cc", "14874784")
                .withDataEntry("limite", "1000.00")
                ;
        return message.toJson();
    }
}
