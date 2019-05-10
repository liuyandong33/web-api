package build.dream.web.auth;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionInformationExpiredStrategy implements org.springframework.security.web.session.SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        int a = 100;
    }
}
