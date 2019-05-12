package build.dream.webapi.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.BackgroundRole;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.webapi.constants.Constants;
import build.dream.webapi.models.role.ListRolesModel;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    /**
     * 分页查询角色
     *
     * @param listRolesModel
     * @return
     */
    public ApiRest listRoles(ListRolesModel listRolesModel) {
        BigInteger tenantId = listRolesModel.getTenantId();
        int page = listRolesModel.getPage();
        int rows = listRolesModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition(BackgroundRole.ColumnName.DELETED, Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));
        searchConditions.add(new SearchCondition(BackgroundRole.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId));
        SearchModel searchModel = new SearchModel();
        searchModel.setSearchConditions(searchConditions);

        long count = DatabaseHelper.count(BackgroundRole.TABLE_NAME, searchModel);
        List<BackgroundRole> backgroundRoles = null;
        if (count > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel();
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);
            backgroundRoles = DatabaseHelper.findAllPaged(BackgroundRole.class, pagedSearchModel);
        } else {
            backgroundRoles = new ArrayList<BackgroundRole>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", backgroundRoles);
        return ApiRest.builder().data(data).message("查询角色成功！").successful(true).build();
    }
}
