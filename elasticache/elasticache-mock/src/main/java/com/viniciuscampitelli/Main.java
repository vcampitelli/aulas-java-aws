package com.viniciuscampitelli;

import com.viniciuscampitelli.mock.Jedis;

/*
 * Não altere essa classe!
 */
public class Main {
    // Não altere esse valor!
    protected final static String cacheKey = "valor_da_chave";

    protected static String buscaDados() {
        // Esse é um valor falso representando o que poderia ser buscado através de uma consulta ao banco de dados
        return "Valor a ser salvo no cache";
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        String valor = Exercicio.process(jedis);
        System.out.println("Valor: " + valor);
    }
}
