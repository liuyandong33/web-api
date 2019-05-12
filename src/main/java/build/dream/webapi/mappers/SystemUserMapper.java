package build.dream.webapi.mappers;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.saas.domains.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SystemUserMapper {
    SystemUser findByLoginNameOrEmailOrMobile(@Param("loginName") String loginName);

    SystemUser findByMobile(@Param("mobile") String mobile);

    List<BackgroundPrivilege> findAllBackgroundPrivileges(@Param("userId") BigInteger userId);
}
