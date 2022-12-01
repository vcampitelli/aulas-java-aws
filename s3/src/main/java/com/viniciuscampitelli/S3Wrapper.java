package com.viniciuscampitelli;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

class S3Wrapper {

    private final Scanner in;

    private final String bucketName;

    private final S3Client s3Client;

    public S3Wrapper(Scanner in) {
        this.in = in;
        Region defaultRegion = DefaultSettings.getRegion();

        System.out.print("Qual o nome do bucket desejado? ");
        bucketName = in.next();

        System.out.printf(
                "Qual a região do bucket? [%s] ",
                defaultRegion.toString()
        );
        String region = in.nextLine();

        s3Client = S3Client.builder()
                .region((region.length() == 0) ? defaultRegion : Region.of(region))
                .credentialsProvider(DefaultSettings.getCredentialsProvider())
                .build();
    }

    public void uploadFile() throws FileNotFoundException {
        System.out.print("Qual o caminho completo do arquivo a ser enviado? ");
        String filename = in.next();
        File file = new File(filename);
        if (!file.isFile()) {
            throw new FileNotFoundException("Arquivo " + filename + " não existe");
        }

        String key = file.getName();
        System.out.print("Enviando arquivo " + key + "... ");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(file)
        );

        System.out.println("OK");
    }

    public void downloadFile() throws IOException {
        System.out.print("Qual o caminho completo do arquivo a ser baixado do bucket? ");
        String s3Filename = in.next();

        System.out.print("Qual o caminho completo para salvar o arquivo? ");
        String localFilename = in.next();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filename)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilename));

        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = response.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        response.close();
        outputStream.close();

        System.out.println("Arquivo " + localFilename + "baixado com sucesso");
    }

    public void listObjects() {
        System.out.println("Listando objetos do bucket " + bucketName + "...");

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsRequest);
        List<S3Object> objects = response.contents();
        System.out.printf(
                "%20s\t%6s\t%s\n",
                "Data",
                "Tamanho",
                "Arquivo"
        );
        for (S3Object myValue : objects) {
            System.out.printf(
                    "%20s\t%6d\t%s\n",
                    myValue.lastModified().toString(),
                    myValue.size(),
                    myValue.key()
            );
        }
    }
}
