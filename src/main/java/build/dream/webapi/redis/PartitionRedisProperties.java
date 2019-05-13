package build.dream.webapi.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "partition.redis")
public class PartitionRedisProperties extends RedisProperties {
}
