package build.dream.web.configurations;

import build.dream.web.auth.SessionRegistryImpl;
import build.dream.web.auth.WebUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private WebUserDetailsService webUserDetailsService;
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().disable()
                .authorizeRequests()
                .antMatchers("/auth/index", "/auth/login", "/login/login", "/login/logout", "/auth/failure").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/index").loginProcessingUrl("/login/login").successForwardUrl("/auth/success").failureUrl("/auth/failure")
                .and()
                .logout().logoutUrl("/login/logout").invalidateHttpSession(true).logoutSuccessUrl("/auth/index")
                .and()
                .sessionManagement().invalidSessionStrategy(invalidSessionStrategy).maximumSessions(1).maxSessionsPreventsLogin(true).expiredSessionStrategy(sessionInformationExpiredStrategy).sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
