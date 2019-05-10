package build.dream.web.auth;

import build.dream.web.utils.RedisUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.SerializationUtils;

import java.util.*;

public class SessionRegistryImpl implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {
    private static final String SESSION_INFORMATION_PREFIX = "_session_information";
    private static final String PRINCIPAL_PREFIX = "_principal";

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        String sessionId = event.getId();
        removeSessionInformation(sessionId);
    }

    @Override
    public List<Object> getAllPrincipals() {
        return Collections.emptyList();
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        WebUserDetails webUserDetails = (WebUserDetails) principal;
        Set<String> sessionsUsedByPrincipal = RedisUtils.smembers(PRINCIPAL_PREFIX + "_" + webUserDetails.getUsername());
        if (sessionsUsedByPrincipal == null) {
            return Collections.emptyList();
        }

        List<SessionInformation> sessionInformationList = new ArrayList<SessionInformation>(sessionsUsedByPrincipal.size());
        for (String sessionId : sessionsUsedByPrincipal) {
            SessionInformation sessionInformation = getSessionInformation(sessionId);
            if (sessionInformation == null) {
                continue;
            }
            if (includeExpiredSessions || !sessionInformation.isExpired()) {
                sessionInformationList.add(sessionInformation);
            }
        }
        return sessionInformationList;
    }

    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        String key = SESSION_INFORMATION_PREFIX + "_" + sessionId;
        String value = RedisUtils.get(key);
        if (StringUtils.isNotBlank(value)) {
            return (SessionInformation) SerializationUtils.deserialize(Base64.decodeBase64(value));
        }
        return null;
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        String key = SESSION_INFORMATION_PREFIX + "_" + sessionId;
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation != null) {
            sessionInformation.refreshLastRequest();
            RedisUtils.set(key, Base64.encodeBase64String(SerializationUtils.serialize(sessionInformation)));
        }
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        WebUserDetails webUserDetails = (WebUserDetails) principal;
        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }

        SessionInformation sessionInformation = new SessionInformation(principal, sessionId, new Date());
        RedisUtils.set(SESSION_INFORMATION_PREFIX + "_" + sessionId, Base64.encodeBase64String(SerializationUtils.serialize(sessionInformation)));
        RedisUtils.sadd(PRINCIPAL_PREFIX + "_" + webUserDetails.getUsername(), sessionId);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation == null) {
            return;
        }

        RedisUtils.delete(SESSION_INFORMATION_PREFIX + "_" + sessionId);

        WebUserDetails webUserDetails = (WebUserDetails) sessionInformation.getPrincipal();
        RedisUtils.srem(PRINCIPAL_PREFIX + "_" + webUserDetails.getUsername(), sessionId);
    }
}
