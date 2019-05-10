package build.dream.web.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuyandong on 2017/3/24.
 */
public class ApplicationHandler {
    private static ApplicationContext applicationContext;
    private static ServletContext servletContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationHandler.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setServletContext(ServletContext servletContext) {
        ApplicationHandler.servletContext = servletContext;
    }

    public static ServletContext obtainServletContext() {
        return servletContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 获取ServletRequestAttributes
     *
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取HttpServletResponse
     *
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        return getServletRequestAttributes().getResponse();
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public static Map<String, String> getRequestParameters() {
        return getRequestParameters(getHttpServletRequest());
    }

    /**
     * 获取请求参数
     *
     * @param httpServletRequest
     * @return
     */
    public static Map<String, String> getRequestParameters(HttpServletRequest httpServletRequest) {
        Map<String, String> requestParameters = new LinkedHashMap<String, String>();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            requestParameters.put(parameterEntry.getKey(), StringUtils.trimToEmpty(StringUtils.join(parameterEntry.getValue(), ",")));
        }
        return requestParameters;
    }

    /**
     * 根据参数名称获取参数
     *
     * @param requestParameterName
     * @return
     */
    public static String getRequestParameter(String requestParameterName) {
        return getRequestParameter(getHttpServletRequest(), requestParameterName);
    }

    /**
     * 根据参数名称获取参数
     *
     * @param httpServletRequest
     * @param requestParameterName
     * @return
     */
    public static String getRequestParameter(HttpServletRequest httpServletRequest, String requestParameterName) {
        return StringUtils.trimToNull(StringUtils.join(httpServletRequest.getParameterValues(requestParameterName), ","));
    }

    /**
     * 获取session id
     *
     * @return
     */
    public static String getSessionId() {
        return getServletRequestAttributes().getSessionId();
    }

    public static HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }
}