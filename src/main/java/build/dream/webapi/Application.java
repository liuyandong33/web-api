package build.dream.webapi;

import build.dream.webapi.auth.RedisSessionRegistryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = RedisSessionRegistryImpl.MAX_INACTIVE_INTERVAL_IN_SECONDS)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
