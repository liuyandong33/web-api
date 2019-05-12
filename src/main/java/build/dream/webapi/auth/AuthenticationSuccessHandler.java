package build.dream.webapi.auth;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (ApplicationHandler.isAjax(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ApiRest apiRest = ApiRest.builder()
                    .message("登录成功！")
                    .successful(true)
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
            printWriter.write("<div style=\"background-color: #00AAEE;height: 50px;text-align: center;padding-top: 10px;\">登录成功！</div>\n");
            printWriter.write("</body>\n");
            printWriter.write("</html>\n");
        }
    }
}
