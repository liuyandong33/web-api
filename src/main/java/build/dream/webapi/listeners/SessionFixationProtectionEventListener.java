package build.dream.webapi.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionFixationProtectionEventListener implements ApplicationListener<SessionFixationProtectionEvent> {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void onApplicationEvent(SessionFixationProtectionEvent event) {
        sessionRegistry.removeSessionInformation(event.getOldSessionId());
    }
}
