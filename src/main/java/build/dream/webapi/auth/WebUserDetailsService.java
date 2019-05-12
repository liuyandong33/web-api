package build.dream.webapi.auth;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.ValidateUtils;
import build.dream.webapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class WebUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = userService.findByLoginNameOrEmailOrMobile(username);
        ValidateUtils.notNull(systemUser, "用户不存在！");

        BigInteger userId = systemUser.getId();

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<BackgroundPrivilege> backgroundPrivileges = userService.findAllBackgroundPrivileges(userId);
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            authorities.add(new SimpleGrantedAuthority(backgroundPrivilege.getPrivilegeCode()));
        }

        WebUserDetails webUserDetails = new WebUserDetails();
        webUserDetails.setUsername(username);
        webUserDetails.setPassword(systemUser.getPassword());
        webUserDetails.setAuthorities(authorities);
        webUserDetails.setUserId(userId);
        return webUserDetails;
    }
}
