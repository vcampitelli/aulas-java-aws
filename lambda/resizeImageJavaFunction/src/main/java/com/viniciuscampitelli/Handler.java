package com.viniciuscampitelli;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.google.gson.Gson;
import org.joda.time.Instant;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<S3Event, String> {
    private S3Client cliente = null;

    private Gson gson = new Gson();

    private final String tempDir = System.getProperty("java.io.tmpdir");

    private final String pastaDestino = "thumbs/";

    public String handleRequest(S3Event event, Context context) {
        S3EventNotificationRecord record = event.getRecords().get(0);

        // Buscando os dados do bucket e caminho do arquivo (key)
        String bucket = record.getS3().getBucket().getName();
        String key = record.getS3().getObject().getUrlDecodedKey();

        // Separando a extensão do nome do arquivo
        int posicaoPontoFinal = key.lastIndexOf(".");
        if (posicaoPontoFinal == -1) {
            throw new RuntimeException("Extensão inválida: " + key);
        }
        String extensao = key.substring(posicaoPontoFinal + 1);
        String arquivoSemExtensao = key.substring(0, posicaoPontoFinal);

        // Vamos salvar o arquivo em /tmp/<nome-do-bucket>_<nome-do-arquivo>_<timestamp>.<extensao>
        // Exemplo: /tmp/meu-bucket_caminho-para-arquivo_1670281333458.jpg
        File arquivoLocal = new File(
                String.format(
                        "%s/%s_%s_%s.%s",
                        tempDir,
                        bucket.replace('_', '-'),
                        arquivoSemExtensao.replace('/', '-').replace('_', '-'),
                        (new Instant()).getMillis(),
                        extensao
                )
        );

        LambdaResponse resposta;
        try {
            // Baixando o arquivo do S3
            BufferedImage imagemOriginal = download(bucket, key);

            // Redimensionando a imagem para 150px de largura
            ByteArrayOutputStream outputStream = redimensionaImagem(imagemOriginal, extensao, 150);

            // Fazendo o upload da imagem redimensionada para um arquivo com final "*-thumb.<extensao>"
            String destino = pastaDestino + arquivoSemExtensao + "-thumb." + extensao;
            upload(
                    bucket,
                    destino,
                    extensao,
                    outputStream
            );

            resposta = LambdaResponse.fromFilepath(destino);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            resposta = LambdaResponse.fromException(e);
        }

        return gson.toJson(resposta);
    }

    private BufferedImage download(String bucket, String key) throws S3Exception, IOException {
        GetObjectRequest requisicaoDownload = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> arquivo = getCliente().getObject(requisicaoDownload);
        return ImageIO.read(arquivo);
    }

    private ByteArrayOutputStream redimensionaImagem(BufferedImage imagem, String extensao, int larguraDesejada) throws IOException {
        int larguraImagem = imagem.getWidth();
        int alturaImagem = imagem.getHeight();

        int alturaDesejada = (int) (((float) larguraDesejada / (float) larguraImagem) * (float) alturaImagem);

        // Objeto que receberá a imagem redimensionada
        BufferedImage imagemRedimensionada = new BufferedImage(
                larguraDesejada,
                alturaDesejada,
                BufferedImage.TYPE_INT_RGB
        );

        // Redimensionamos a imagem para o tamanho desejado
        Image imagemRedimensionadaAsync = imagem.getScaledInstance(
                larguraDesejada,
                alturaDesejada,
                BufferedImage.SCALE_SMOOTH
        );

        // A imagem é redimensionada assincronamente para economizar recursos, então aqui forçamos para que ela seja
        // renderizada nesse momento para podermos fazer o upload
        Graphics graphics = imagemRedimensionada.getGraphics();
        graphics.drawImage(imagemRedimensionadaAsync, 0, 0, null);
        graphics.dispose();

        // Escrevemos o conteúdo da imagem em memória
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(imagemRedimensionada, extensao, outputStream);

        return outputStream;
    }

    private PutObjectResponse upload(String bucket, String key, String extensao, ByteArrayOutputStream imagem) {
        String contentType = "application/octet-stream";
        if ((extensao.equals("jpg")) || (extensao.equals("jpeg"))) {
            contentType = "image/jpeg";
        } else if (extensao.equals("png")) {
            contentType = "image/png";
        }

        PutObjectRequest requisicaoUpload = PutObjectRequest.builder()
                .bucket(bucket)
                .contentLength((long) imagem.size())
                .contentType(contentType)
                .key(key)
                .build();

        return cliente.putObject(
                requisicaoUpload,
                RequestBody.fromBytes(imagem.toByteArray())
        );
    }

    private S3Client getCliente() {
        if (cliente == null) {
            cliente = S3Client.builder().build();
        }
        return cliente;
    }
}
