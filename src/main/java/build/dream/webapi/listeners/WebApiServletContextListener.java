package build.dream.webapi.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.webapi.mappers.CommonMapper;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebApiServletContextListener extends BasicServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);
    }
}
