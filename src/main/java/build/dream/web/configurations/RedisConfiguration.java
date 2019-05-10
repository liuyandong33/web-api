package build.dream.web.configurations;

import build.dream.web.redis.UniversalRedisTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfiguration {
    @Bean
    public UniversalRedisTemplate customRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new UniversalRedisTemplate(redisConnectionFactory);
    }
}
