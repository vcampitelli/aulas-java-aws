package com.viniciuscampitelli;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String queueUrl = ""; // @FIXME forneça a URL da fila
        Region region = Region.US_EAST_1;
        SqsClient sqsClient = SqsClient.builder()
                .region(region)
                .build();

        Scanner in = new Scanner(System.in);
        ReceiveMessage receiver = null;
        SendMessage sender = null;

        int option = -1;

        while (option != 0) {
            System.out.println();
            System.out.println("Listagem de comandos");
            System.out.println("  [1] Enviar mensagens");
            System.out.println("  [2] Receber mensagens");
            System.out.println("  [0] Sair");

            System.out.println();
            System.out.print("Digite o número do comando: ");
            option = in.nextInt();
            System.out.println();

            switch (option) {
                case 1:
                    in.nextLine(); // lendo o ENTER após o "número do comando"
                    System.out.print("Mensagem a ser enviada: ");
                    String body = in.nextLine();
                    if (sender == null) {
                        sender = new SendMessage(sqsClient);
                    }
                    sender.send(queueUrl, body);
                    break;

                case 2:
                    if (receiver == null) {
                        receiver = new ReceiveMessage(sqsClient);
                    }
                    receiver.receive(queueUrl);
                    break;
            }
        }
    }
}
