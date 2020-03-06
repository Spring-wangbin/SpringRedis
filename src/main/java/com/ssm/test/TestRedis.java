package com.ssm.test;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestRedis {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost",6379);

        Map<String,String> user = new HashMap<String,String>();
        user.put("name","wangbin");
        user.put("age","23");
        user.put("tel","666666");

        jedis.hmset("user",user);

        Iterator<String> it = jedis.hkeys("user").iterator();
        while (it.hasNext()){
            String field = it.next();
            System.out.println(jedis.hget("user",field));
        }
    }
}
