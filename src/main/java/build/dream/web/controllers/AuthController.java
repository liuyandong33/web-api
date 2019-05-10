package build.dream.web.controllers;

import build.dream.web.constants.Constants;
import build.dream.web.utils.ApplicationHandler;
import build.dream.web.utils.WebSecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/index");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String login() {
        try {
            HttpServletRequest httpServletRequest = ApplicationHandler.getHttpServletRequest();
            Map<String, String> requestParameters = ApplicationHandler.getRequestParameters(httpServletRequest);

            String loginName = requestParameters.get("loginName");
            String password = requestParameters.get("password");

            WebSecurityUtils.authorize(loginName, password, httpServletRequest);

            return Constants.SUCCESS;
        } catch (Exception e) {
            return Constants.FAILURE;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public String logout() {
        HttpSession httpSession = ApplicationHandler.getHttpSession();
        httpSession.invalidate();
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/success")
    @ResponseBody
    public String success() {
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/failure")
    @ResponseBody
    public String failure() {
        return Constants.FAILURE;
    }
}
