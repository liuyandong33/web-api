package build.dream.webapi.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.webapi.models.sms.SendVerificationCodeModel;
import build.dream.webapi.services.SmsService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sms")
public class SmsController {
    /**
     * 分页查询角色
     *
     * @return
     */
    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SendVerificationCodeModel.class, serviceClass = SmsService.class, serviceMethodName = "sendVerificationCode", error = "发送短信验证码")
    public String sendVerificationCode() {
        return null;
    }
}
