package build.dream.web.utils;

import build.dream.web.redis.UniversalRedisTemplate;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UniversalRedisUtils {
    private static UniversalRedisTemplate universalRedisTemplate = null;

    public static UniversalRedisTemplate obtainUniversalRedisTemplate() {
        if (universalRedisTemplate == null) {
            universalRedisTemplate = ApplicationHandler.getBean(UniversalRedisTemplate.class);
        }
        return universalRedisTemplate;
    }

    public static ValueOperations<String, Object> obtainValueOperations() {
        return obtainUniversalRedisTemplate().opsForValue();
    }

    public static HashOperations<String, String, Object> obtainHashOperations() {
        return obtainUniversalRedisTemplate().opsForHash();
    }

    public static ListOperations<String, Object> obtainListOperations() {
        return obtainUniversalRedisTemplate().opsForList();
    }

    public static SetOperations<String, Object> obtainSetOperations() {
        return obtainUniversalRedisTemplate().opsForSet();
    }

    public static ZSetOperations<String, Object> obtainZSetOperations() {
        return obtainUniversalRedisTemplate().opsForZSet();
    }

    /**
     * KEYS
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        return obtainUniversalRedisTemplate().keys(pattern);
    }

    /**
     * SET
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        obtainValueOperations().set(key, value);
    }

    /**
     * SETEX
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public static void setex(String key, Object value, long timeout, TimeUnit unit) {
        obtainValueOperations().set(key, value, timeout, unit);
    }

    /**
     * SETNX
     *
     * @param key
     * @param value
     * @return
     */
    public static Boolean setnx(String key, Object value) {
        return obtainValueOperations().setIfAbsent(key, value);
    }

    /**
     * MSET
     *
     * @param map
     */
    public static void mset(Map<String, Object> map) {
        obtainValueOperations().multiSet(map);
    }

    /**
     * MSETNX
     *
     * @param map
     * @return
     */
    public static Boolean msetnx(Map<String, Object> map) {
        return obtainValueOperations().multiSetIfAbsent(map);
    }

    /**
     * GET
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return obtainValueOperations().get(key);
    }
}
