package com.viniciuscampitelli;

public class LambdaResponse {
    private String caminho;

    private String erro;

    private boolean status = true;

    private LambdaResponse(String caminhoOuErro, boolean status) {
        if (status) {
            this.caminho = caminhoOuErro;
        } else {
            this.erro = caminhoOuErro;
        }
        this.status = status;
    }

    public static LambdaResponse fromFilepath(String filename) {
        return new LambdaResponse(filename, true);
    }

    public static LambdaResponse fromException(Exception e) {
        return new LambdaResponse(e.getMessage(), false);
    }
}
