package build.dream.web.services;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrivilegeService {
    @Transactional(readOnly = true)
    public List<BackgroundPrivilege> listBackgroundPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(BackgroundPrivilege.class, searchModel);
    }
}
