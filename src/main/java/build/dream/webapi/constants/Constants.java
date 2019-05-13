package build.dream.webapi.constants;

/**
 * Created by liuyandong on 2017/7/24.
 */
public class Constants extends build.dream.common.constants.Constants {
    public static final String LOGIN_MODE = "loginMode";
    public static final String LOGIN_MODE_PASSWORD = "PASSWORD";
    public static final String LOGIN_MODE_SMS_VERIFICATION_CODE = "SMS_VERIFICATION_CODE";

    public static final String AUTHENTICATED = "authenticated";
    public static final String PERMIT_ALL = "permitAll";
    public static final String HAS_AUTHORITY_FORMAT = "hasAuthority('%s')";

    public static final String SMS_VERIFICATION_CODE_PREFIX = "_sms_verification_code";

    public static final String REDIS_TEMPLATE = "redisTemplate";
    public static final String COMMON_STRING_REDIS_TEMPLATE = "commonStringRedisTemplate";
    public static final String PARTITION_STRING_REDIS_TEMPLATE = "partitionStringRedisTemplate";

    public static final String COMMON_REDIS_CONNECTION_FACTORY = "commonRedisConnectionFactory";
    public static final String PARTITION_REDIS_CONNECTION_FACTORY = "partitionRedisConnectionFactory";
}
