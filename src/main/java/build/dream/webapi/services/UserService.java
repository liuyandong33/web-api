package build.dream.webapi.services;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.saas.domains.SystemUser;
import build.dream.webapi.mappers.SystemUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Transactional(readOnly = true)
    public SystemUser findByLoginNameOrEmailOrMobile(String loginName) {
        return systemUserMapper.findByLoginNameOrEmailOrMobile(loginName);
    }

    @Transactional(readOnly = true)
    public SystemUser findByMobile(String loginName) {
        return systemUserMapper.findByMobile(loginName);
    }

    @Transactional(readOnly = true)
    public List<BackgroundPrivilege> findAllBackgroundPrivileges(BigInteger userId) {
        return systemUserMapper.findAllBackgroundPrivileges(userId);
    }
}
