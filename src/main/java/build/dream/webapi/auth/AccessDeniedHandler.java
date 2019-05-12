package build.dream.webapi.auth;

import build.dream.common.api.ApiRest;
import build.dream.common.exceptions.Error;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.webapi.constants.Constants;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        if (ApplicationHandler.isAjax(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(Constants.ACCESS_DENIED_ERROR_API_REST);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write("<html>\n");
            printWriter.write("<head>");
            printWriter.write("<title>错误页面</title>\n");
            printWriter.write("<style type=\"text/css\">\n");
            printWriter.write("body {margin: 0;}\n");
            printWriter.write("</style>\n");
            printWriter.write("</head>\n");
            printWriter.write("<body>\n");
            printWriter.write("<div style=\"background-color: yellow;height: 50px;text-align: center;padding-top: 10px;\">不允许访问！</div>\n");
            printWriter.write("</body>\n");
            printWriter.write("</html>\n");
        }
    }
}
