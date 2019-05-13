package build.dream.webapi.models.sms;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class SendVerificationCodeModel extends BasicModel {
    @NotNull
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
