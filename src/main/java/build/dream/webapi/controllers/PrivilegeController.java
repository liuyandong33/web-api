package build.dream.webapi.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.webapi.models.privilege.ListBackgroundPrivilegesModel;
import build.dream.webapi.services.PrivilegeService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/privilege")
public class PrivilegeController {
    /**
     * 查询所有权限
     *
     * @return
     */
    @RequestMapping(value = "/listBackgroundPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListBackgroundPrivilegesModel.class, serviceClass = PrivilegeService.class, serviceMethodName = "listBackgroundPrivileges", error = "查询权限失败")
    public String listBackgroundPrivileges() {
        return null;
    }
}
