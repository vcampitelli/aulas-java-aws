package com.viniciuscampitelli;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.joda.time.Instant;

// Não altere essa classe, ela só é invocada localmente
public class Main {
    public static void main(String[] args) {
        // Para testar localmente sem instalar o sam, criei uma classe que imita o evento
        // do S3 e outra pro contexto

        S3Event event = S3EventBuilder.build(
                "<nome-do-seu-bucket>",
                "caminho/para/imagem.png"
        );

        Handler handler = new Handler();
        String response = handler.handleRequest(event, new FakeContext());

        System.out.println("Response: " + response);
    }
}
