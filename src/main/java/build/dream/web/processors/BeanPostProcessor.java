package build.dream.web.processors;

import org.springframework.beans.BeansException;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

@Component
public class BeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {
    private FilterSecurityInterceptor filterSecurityInterceptor;
    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    private boolean isSetFinish = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof FilterSecurityInterceptor) {
            filterSecurityInterceptor = (FilterSecurityInterceptor) bean;
        }

        if (bean instanceof FilterInvocationSecurityMetadataSource) {
            filterInvocationSecurityMetadataSource = (FilterInvocationSecurityMetadataSource) bean;
        }

        if (filterSecurityInterceptor != null && filterInvocationSecurityMetadataSource != null && !isSetFinish) {
            filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
            isSetFinish = true;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
