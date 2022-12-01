package com.viniciuscampitelli;

import software.amazon.awssdk.core.SdkBytes;

public class Main {
    // @FIXME forneça o ID da chave recebido
    private static final String keyId = "";

    public static void main(String[] args) {
        // @FIXME forneça o código criptografado recebido
        String ciphertext = "";

        // Esse método converte a string acima na classe SdkBytes necessária para a requisição do SDK
        SdkBytes sdkBytes = KmsHelper.stringToSdkBytes(ciphertext);

        // @TODO criar KmsClient e invocar o método de descriptografar o código, gerando uma variável chamada "resposta"

        System.out.println(
                "Texto decifrado: " + KmsHelper.decryptionToString(resposta)
        );
    }
}