package build.dream.web.auth;

import build.dream.web.constants.Constants;
import build.dream.web.utils.ApplicationHandler;
import build.dream.web.utils.RedisUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class WebUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = null;
        String loginMode = ApplicationHandler.getRequestParameter("loginMode");
        if (Constants.LOGIN_MODE_PASSWORD.equals(loginMode)) {
            password = "$2a$10$zK91v1sV0bIPpA5lcRQA4.qPBH102zlSdTtwP3l1CjUMJxGLJd5mu";
        } else if (Constants.LOGIN_MODE_SMS_VERIFICATION_CODE.equals(loginMode)) {
            String sessionId = ApplicationHandler.getSessionId();
            String verificationCode = RedisUtils.get(sessionId);
            Validate.notNull(verificationCode, "短信验证码已过期！");

            password = passwordEncoder.encode(verificationCode);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else if ("user".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("user"));
        }

        WebUserDetails webUserDetails = new WebUserDetails();
        webUserDetails.setUsername(username);
        webUserDetails.setPassword(password);
        webUserDetails.setAuthorities(authorities);
        return webUserDetails;
    }
}
