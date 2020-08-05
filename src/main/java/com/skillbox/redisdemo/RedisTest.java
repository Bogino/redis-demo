package com.skillbox.redisdemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class RedisTest {

    // Запуск докер-контейнера:
    // docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis

    // Для теста будем считать неактивными пользователей, которые не заходили 2 секунды
    private static final int DELETE_SECONDS_AGO = 2;

    // Допустим пользователи делают 500 запросов к сайту в секунду
    private static final int RPS = 20;

    // И всего на сайт заходило 1000 различных пользователей
    private static final int USERS = 20;

    // Также мы добавим задержку между посещениями
    private static final int SLEEP = 1; // 1 миллисекунда

    private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm:ss");

    private static void log(int UsersOnline) {
        String log = String.format("[%s] Пользователей онлайн: %d", DF.format(new Date()), UsersOnline);
        out.println(log);
    }

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();

        for(int request = 0; request <= RPS; request++) {
            int user_id = request;
            redis.logPageVisit(user_id);
            Thread.sleep(SLEEP);
        }
        redis.deleteOldEntries(DELETE_SECONDS_AGO);
        redis.listKeys();
        redis.shutdown();
    }
}
