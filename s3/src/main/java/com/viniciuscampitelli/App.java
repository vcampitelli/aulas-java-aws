package com.viniciuscampitelli;

import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.util.Scanner;

class App {

    public static void main(String[] args) {
        S3Wrapper s3 = null;
        try {
            Scanner in = new Scanner(System.in);
            s3 = new S3Wrapper(in);

            int option = -1;

            while (option != 0) {
                System.out.println();
                System.out.println("Listagem de comandos");
                System.out.println("  [1] Listar objetos");
                System.out.println("  [2] Subir arquivo");
                System.out.println("  [3] Baixar arquivo");
                System.out.println("  [0] Sair");

                System.out.println();
                System.out.print("Digite um comando de 1 a 3 ou 0 para sair: ");
                option = in.nextInt();
                System.out.println();

                switch (option) {
                    case 1 -> s3.listObjects();
                    case 2 -> s3.uploadFile();
                    case 3 -> s3.downloadFile();
                }
            }
        } catch (AwsServiceException e) {
            System.err.println(e.awsErrorDetails().errorCode());
            System.err.println(e.awsErrorDetails().errorMessage());
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(2);
        }
    }
}
