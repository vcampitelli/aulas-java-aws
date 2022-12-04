package com.viniciuscampitelli;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

public class Main {
    // @FIXME forneça o ID da chave recebido
    private static final String keyId = "";

    public static void main(String[] args) {
        // @FIXME forneça o código criptografado recebido
        String ciphertext = "";

        Region region = Region.US_EAST_1;
        KmsClient cliente = KmsClient.builder()
                .region(region)
                .build();

        SdkBytes sdkBytes = KmsHelper.stringToSdkBytes(ciphertext);

        DecryptRequest requisicao = DecryptRequest.builder()
                .keyId(keyId)
                .ciphertextBlob(sdkBytes)
                .build();

        DecryptResponse resposta = cliente.decrypt(requisicao);

        System.out.println(
                "Texto decifrado: " + KmsHelper.decryptionToString(resposta)
        );
    }
}
