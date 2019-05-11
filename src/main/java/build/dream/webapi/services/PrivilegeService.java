package build.dream.webapi.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.webapi.beans.ZTreeNode;
import build.dream.webapi.constants.Constants;
import build.dream.webapi.models.privilege.ListBackgroundPrivilegesModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {
    /**
     * 查询所有权限
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listBackgroundPrivileges(ListBackgroundPrivilegesModel listBackgroundPrivilegesModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.SERVICE_NAME_WEBAPI);
        List<BackgroundPrivilege> backgroundPrivileges = DatabaseHelper.findAll(BackgroundPrivilege.class, searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(backgroundPrivilege.getId().toString(), backgroundPrivilege.getPrivilegeName(), backgroundPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return ApiRest.builder().data(zTreeNodes).message("获取后台权限列表成功！").successful(true).build();
    }
}
