package build.dream.web.utils;

import build.dream.web.constants.Constants;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;

public class WebSecurityUtils {
    public static void authorize(String principal) {
        authorize(principal, Constants.SECURITY_DEFAULT_PASSWORD);
    }

    public static void authorize(String principal, String credentials) {
        authorize(principal, credentials, ApplicationHandler.getHttpServletRequest(), ApplicationHandler.getHttpServletResponse());
    }

    public static void authorize(String principal, String credentials, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(httpServletRequest.getServletContext());

        AuthenticationManager authenticationManager = webApplicationContext.getBean(AuthenticationManager.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> processMap(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap, ExpressionParser expressionParser) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ExpressionBasedFilterInvocationSecurityMetadataSource> expressionBasedFilterInvocationSecurityMetadataSourceClass = ExpressionBasedFilterInvocationSecurityMetadataSource.class;
        Method expressionBasedFilterInvocationSecurityMetadataSourceClassProcessMapMethod = expressionBasedFilterInvocationSecurityMetadataSourceClass.getDeclaredMethod("processMap", LinkedHashMap.class, ExpressionParser.class);
        expressionBasedFilterInvocationSecurityMetadataSourceClassProcessMapMethod.setAccessible(true);
        return (LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>) expressionBasedFilterInvocationSecurityMetadataSourceClassProcessMapMethod.invoke(null, requestMap, expressionParser);
    }
}
