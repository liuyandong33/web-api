package build.dream.web.controllers;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.web.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/privilege")
public class PrivilegeController {
    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping(value = "/listBackgroundPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<BackgroundPrivilege> listBackgroundPrivileges() {
        return privilegeService.listBackgroundPrivileges();
    }
}
