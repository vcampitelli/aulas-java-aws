package com.viniciuscampitelli;

import com.google.gson.Gson;
import com.viniciuscampitelli.entity.Address;
import com.viniciuscampitelli.entity.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    private static JedisPool pool = null;

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (JedisPool pool = getJedisPool()) {
            Jedis jedis = pool.getResource();

            User user = buscarUsuario(jedis, 1234);

            System.out.println("Usuário retornado:");
            System.out.println("      ID: " + user.getId());
            System.out.println("    Name: " + user.getName());
            System.out.println("Username: " + user.getUsername());
        } catch (IOException | JedisException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static User buscarUsuario(Jedis jedis, Integer idUsuario) {
        String cacheKey = "usuario_" + idUsuario;

        // Se houver os dados do usuário no Redis, retorna de lá
        if (jedis.exists(cacheKey)) {
            System.out.println("Buscando do Redis..." );
            String cached = jedis.get(cacheKey);

            User user = gson.fromJson(cached, User.class);
            return user;
        }

        // Senão, busca do banco de dados...
        System.out.println("Buscando da fonte..." );
        User user = buscarUsuarioPorId(idUsuario);

        // E salva no Redis para as próximas requisições
        String json = gson.toJson(user);
        jedis.setex(cacheKey, 30, json);

        return user;
    }

    private static User buscarUsuarioPorId(Integer idUsuario) {
        /*
         Em um código real, chamaríamos as classes de repositório para buscar os dados do usuário, algo como:

         UserRepository userRepository = new UserRepository()
         User user = userRepository.findById(idUsuario);

         AddressRepository addressRepository = new AddressRepository()
         List<Address> addresses = AddressRepository.findByUserId(idUsuario);
         */

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("Av Paulista", 1578));
        addresses.add(new Address("Av. Lázaro de Mello Brandão", 300));
        return new User(
                idUsuario,
                "José Silva",
                "ze.silva",
                addresses
        );
    }

    private static JedisPool getJedisPool() throws JedisException, IOException {
        if (pool == null) {
            Properties props = new Properties();
            props.load(new FileInputStream("redis.properties" ));
            pool = new JedisPool(
                    new JedisPoolConfig(),
                    props.getProperty("JAVA_REDIS_HOST"), // endereço do redis (ex: localhost)
                    Integer.parseInt(props.getProperty("JAVA_REDIS_PORT")), // porta (ex: 6379)
                    3000, // timeout de conexão
//                    props.getProperty("JAVA_REDIS_PASSWORD"), // se seu Redis tiver senha, descomente aqui
                    Boolean.parseBoolean(props.getProperty("JAVA_REDIS_SSL")) // se queremos SSL ou não
            );
        }
        return pool;
    }
}