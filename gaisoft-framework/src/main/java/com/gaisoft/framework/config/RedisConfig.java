package com.gaisoft.framework.config;

import com.gaisoft.framework.config.FastJson2JsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig
extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<Object>(Object.class);
        template.setKeySerializer((RedisSerializer)new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer((RedisSerializer)new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptText(this.limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    private String limitScriptText() {
        return "local key = KEYS[1]\nlocal count = tonumber(ARGV[1])\nlocal time = tonumber(ARGV[2])\nlocal current = redis.call('get', key);\nif current and tonumber(current) > count then\n    return tonumber(current);\nend\ncurrent = redis.call('incr', key)\nif tonumber(current) == 1 then\n    redis.call('expire', key, time)\nend\nreturn tonumber(current);";
    }
}
