package build.dream.webapi.auth;

import build.dream.common.api.ApiRest;
import build.dream.common.exceptions.Error;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String errorMessage = null;

        if (exception instanceof BadCredentialsException) {
            errorMessage = "密码错误！";
        } else {
            errorMessage = exception.getMessage();
        }

        if (ApplicationHandler.isAjax(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ApiRest apiRest = ApiRest.builder()
                    .error(new Error("", errorMessage))
                    .successful(false)
                    .build();
            response.getWriter().write(GsonUtils.toJson(apiRest));
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write("<html>\n");
            printWriter.write("<head>");
            printWriter.write("<title>登录成功</title>\n");
            printWriter.write("<style type=\"text/css\">\n");
            printWriter.write("body {margin: 0;}\n");
            printWriter.write("</style>\n");
            printWriter.write("</head>\n");
            printWriter.write("<body>\n");
            printWriter.write("<div style=\"background-color: yellow;height: 50px;text-align: center;padding-top: 10px;\">" + errorMessage + "</div>\n");
            printWriter.write("</body>\n");
            printWriter.write("</html>\n");
        }
    }
}
