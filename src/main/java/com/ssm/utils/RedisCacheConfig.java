package com.ssm.utils;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.crypto.KeyGenerator;
import java.lang.reflect.Method;

/**
 * 通过spring管理redis缓存配置
 *
 * @author Administrator
 *
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    private volatile JedisConnectionFactory jedisConnectionFactory;
    private volatile RedisTemplate<String,String> redisTemplate;
    private volatile RedisCacheManager redisCacheManager;

    public RedisCacheConfig(){
        super();
    }

    public RedisCacheConfig(JedisConnectionFactory jedisConnectionFactory, RedisTemplate<String, String> redisTemplate, RedisCacheManager redisCacheManager) {
        this.jedisConnectionFactory = jedisConnectionFactory;
        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
    }

    public JedisConnectionFactory getJedisConnectionFactory() {
        return jedisConnectionFactory;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisCacheManager getRedisCacheManager() {
        return redisCacheManager;
    }

    @Bean
    public KeyGenerator customKeyGenerator(){
        return new KeyGenerator(){
            public Object generate(Object target, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
}
