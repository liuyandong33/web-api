package build.dream.webapi.configurations;

import build.dream.webapi.constants.Constants;
import build.dream.webapi.redis.CommonRedisProperties;
import build.dream.webapi.redis.PartitionRedisProperties;
import build.dream.webapi.utils.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfiguration {
    @Autowired
    private CommonRedisProperties commonRedisProperties;
    @Autowired
    private PartitionRedisProperties partitionRedisProperties;

    @Primary
    @Bean(name = Constants.COMMON_REDIS_CONNECTION_FACTORY)
    public RedisConnectionFactory commonRedisConnectionFactory() {
        return RedisHelper.createRedisConnectionFactory(commonRedisProperties);
    }

    @Bean(name = Constants.PARTITION_REDIS_CONNECTION_FACTORY)
    public RedisConnectionFactory partitionRedisConnectionFactory() {
        return RedisHelper.createRedisConnectionFactory(partitionRedisProperties);
    }

    @Primary
    @Bean(name = Constants.REDIS_TEMPLATE)
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(commonRedisConnectionFactory());
        return redisTemplate;
    }

    @Primary
    @Bean(name = Constants.COMMON_STRING_REDIS_TEMPLATE)
    public StringRedisTemplate commonStringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(commonRedisConnectionFactory());
        return stringRedisTemplate;
    }

    @Bean(name = Constants.PARTITION_STRING_REDIS_TEMPLATE)
    public StringRedisTemplate partitionStringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(partitionRedisConnectionFactory());
        return stringRedisTemplate;
    }
}
