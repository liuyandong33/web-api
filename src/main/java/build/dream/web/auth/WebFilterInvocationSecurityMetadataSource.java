package build.dream.web.auth;

import build.dream.web.utils.WebSecurityUtils;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;

public class WebFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {
    public WebFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        super(WebSecurityUtils.processMap(requestMap, new SpelExpressionParser()));
    }
}
