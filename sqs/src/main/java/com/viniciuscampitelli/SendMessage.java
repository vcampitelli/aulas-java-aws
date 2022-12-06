package com.viniciuscampitelli;

import com.google.gson.Gson;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.Date;

public class SendMessage {
    private final SqsClient sqsClient;

    private final Gson gson = new Gson();

    public SendMessage(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void send(String queueUrl, String body) throws SqsException {
        MessagePacket object = new MessagePacket(body, new Date());

        String json = gson.toJson(object);

        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(json)
                .build();
        System.out.println("\n  Mensagem enviada: " + json);
        sqsClient.sendMessage(sendMsgRequest);
    }
}
