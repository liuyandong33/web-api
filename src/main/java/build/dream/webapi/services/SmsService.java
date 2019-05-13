package build.dream.webapi.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.CommonRedisUtils;
import build.dream.webapi.constants.Constants;
import build.dream.webapi.models.sms.SendVerificationCodeModel;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsService {
    public ApiRest sendVerificationCode(SendVerificationCodeModel sendVerificationCodeModel) {
        String mobile = sendVerificationCodeModel.getMobile();
        String verificationCode = RandomStringUtils.randomNumeric(6);
        CommonRedisUtils.setex(Constants.SMS_VERIFICATION_CODE_PREFIX + "_" + mobile, verificationCode, 5, TimeUnit.MINUTES);
        return ApiRest.builder().message("短信验证码发送成功！").successful(true).build();
    }
}
