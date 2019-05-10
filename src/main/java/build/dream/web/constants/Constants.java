package build.dream.web.constants;

import java.nio.charset.Charset;

/**
 * Created by liuyandong on 2017/7/24.
 */
public class Constants {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    /**
     * 字符集相关常量
     */
    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    public static final Charset CHARSET_GBK = Charset.forName("GBK");
    public static final Charset CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final String CHARSET_NAME_UTF_8 = "UTF-8";
    public static final String CHARSET_NAME_GBK = "GBK";
    public static final String CHARSET_NAME_ISO_8859_1 = "ISO-8859-1";

    public static final String SECURITY_DEFAULT_PASSWORD = "0";

    public static final String LOGIN_MODE = "loginMode";
    public static final String LOGIN_MODE_PASSWORD = "PASSWORD";
    public static final String LOGIN_MODE_SMS_VERIFICATION_CODE = "SMS_VERIFICATION_CODE";

    public static final String AUTHENTICATED = "authenticated";
    public static final String PERMIT_ALL = "permitAll";
    public static final String HAS_AUTHORITY_FORMAT = "hasAuthority('%s')";
}
