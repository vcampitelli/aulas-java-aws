package com.viniciuscampitelli;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

import java.math.BigInteger;

public class KmsHelper {
    public static SdkBytes stringToSdkBytes(String ciphertext) {
        byte[] bytes = new BigInteger(ciphertext, 16).toByteArray();
        return SdkBytes.fromByteArray(bytes);
    }

    public static String decryptionToString(DecryptResponse resposta) {
        return resposta.plaintext().asUtf8String();
    }
}
