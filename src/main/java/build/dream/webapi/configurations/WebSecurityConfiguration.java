package build.dream.webapi.configurations;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.webapi.auth.RedisSessionRegistryImpl;
import build.dream.webapi.auth.RedisTokenRepositoryImpl;
import build.dream.webapi.auth.WebFilterInvocationSecurityMetadataSource;
import build.dream.webapi.auth.WebUserDetailsService;
import build.dream.webapi.constants.Constants;
import build.dream.webapi.services.PrivilegeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private WebUserDetailsService webUserDetailsService;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private PrivilegeService privilegeService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().disable()
                .authorizeRequests()
                .antMatchers("/auth/index", "/auth/login", "/auth/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/index").loginProcessingUrl("/auth/login").successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler)
                .and()
                .logout().logoutUrl("/auth/logout").invalidateHttpSession(true).logoutSuccessUrl("/auth/index")
                .and()
                .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry())
                .and()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(RedisTokenRepositoryImpl.TOKEN_VALIDITY_SECONDS)
                .userDetailsService(webUserDetailsService)
                .tokenRepository(tokenRepository());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new RedisSessionRegistryImpl();
        return sessionRegistry;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(webUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private AntPathRequestMatcher buildAntPathRequestMatcher(String pattern) {
        return new AntPathRequestMatcher(pattern);
    }

    private AntPathRequestMatcher buildAntPathRequestMatcher(String controllerName, String actionName) {
        return new AntPathRequestMatcher("/" + controllerName + "/" + actionName);
    }

    private List<ConfigAttribute> buildPermitAllConfigAttributes() {
        List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(Constants.PERMIT_ALL));
        return configAttributes;
    }

    private List<ConfigAttribute> buildAuthenticatedConfigAttributes() {
        List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(Constants.AUTHENTICATED));
        return configAttributes;
    }

    private List<ConfigAttribute> buildHasAuthorityConfigAttributes(String privilegeCode) {
        List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        configAttributes.add(new SecurityConfig(String.format(Constants.HAS_AUTHORITY_FORMAT, privilegeCode)));
        return configAttributes;
    }

    @Bean
    public WebFilterInvocationSecurityMetadataSource webFilterInvocationSecurityMetadataSource() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        requestMap.put(buildAntPathRequestMatcher("/auth/index"), buildPermitAllConfigAttributes());
        requestMap.put(buildAntPathRequestMatcher("/auth/login"), buildPermitAllConfigAttributes());
        requestMap.put(buildAntPathRequestMatcher("/auth/logout"), buildPermitAllConfigAttributes());
        requestMap.put(buildAntPathRequestMatcher("/sms/sendVerificationCode"), buildPermitAllConfigAttributes());

        List<BackgroundPrivilege> backgroundPrivileges = privilegeService.obtainAllBackgroundPrivileges();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            String controllerName = backgroundPrivilege.getControllerName();
            String actionName = backgroundPrivilege.getActionName();
            if (StringUtils.isNotBlank(controllerName) && StringUtils.isNotBlank(actionName)) {
                requestMap.put(buildAntPathRequestMatcher(controllerName, actionName), buildHasAuthorityConfigAttributes(backgroundPrivilege.getPrivilegeCode()));
            }
        }

        requestMap.put(AnyRequestMatcher.INSTANCE, buildAuthenticatedConfigAttributes());

        return new WebFilterInvocationSecurityMetadataSource(requestMap);
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        PersistentTokenRepository tokenRepository = new RedisTokenRepositoryImpl();
        return tokenRepository;
    }
}
