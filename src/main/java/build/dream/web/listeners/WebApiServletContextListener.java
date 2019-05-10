package build.dream.web.listeners;

import build.dream.web.utils.ApplicationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebApiServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        ApplicationHandler.setServletContext(servletContext);
        ApplicationHandler.setApplicationContext(applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
