package com.viniciuscampitelli;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.InvalidMessageContentsException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

public class ReceiveMessage {
    private final SqsClient sqsClient;

    private final Gson gson = new Gson();

    public ReceiveMessage(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void receive(String queueUrl) throws SqsException {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

        if (messages.size() == 0) {
            System.out.println("Nenhuma mensagem recebida");
        }

        for (Message m : messages) {
            System.out.println("\t Mensagem: " + m.body());
            try {
                MessagePacket packet = gson.fromJson(m.body(), MessagePacket.class);
                System.out.println("\t       ID: " + packet.getId());
                System.out.println("\t     Data: " + packet.getDate().toString());
                System.out.println("\t    Corpo: " + packet.getContents());
            } catch (JsonSyntaxException e) {
                System.out.println("\t Erro ao receber mensagem: " + e.getMessage());
            }
        }
    }
}
