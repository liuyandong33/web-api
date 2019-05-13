package build.dream.webapi.auth;

import build.dream.common.utils.CommonRedisUtils;
import build.dream.webapi.constants.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.*;

public class SessionRegistryImpl implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {
    private static final String SESSION_INFORMATION_PREFIX = "_session_information";
    private static final String PRINCIPAL_PREFIX = "_principal";

    private String obtainPrincipalKey(String username) {
        return PRINCIPAL_PREFIX + "_" + username;
    }

    private String obtainSessionInformationKey(String sessionId) {
        return SESSION_INFORMATION_PREFIX + "_" + sessionId;
    }

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
        Set<String> sessionsUsedByPrincipal = CommonRedisUtils.smembers(obtainPrincipalKey(webUserDetails.getUsername()));
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
        byte[] value = CommonRedisUtils.get(obtainSessionInformationKey(sessionId).getBytes(Constants.CHARSET_UTF_8));
        if (ArrayUtils.isNotEmpty(value)) {
            return (SessionInformation) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation != null) {
            sessionInformation.refreshLastRequest();
            CommonRedisUtils.set(obtainSessionInformationKey(sessionId).getBytes(Constants.CHARSET_UTF_8), SerializationUtils.serialize(sessionInformation));
        }
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        WebUserDetails webUserDetails = (WebUserDetails) principal;
        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }

        SessionInformation sessionInformation = new SessionInformation(principal, sessionId, new Date());
        CommonRedisUtils.set(obtainSessionInformationKey(sessionId).getBytes(Constants.CHARSET_UTF_8), SerializationUtils.serialize(sessionInformation));
        CommonRedisUtils.sadd(obtainPrincipalKey(webUserDetails.getUsername()), sessionId);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation == null) {
            return;
        }

        CommonRedisUtils.del(obtainSessionInformationKey(sessionId).getBytes(Constants.CHARSET_UTF_8));

        WebUserDetails webUserDetails = (WebUserDetails) sessionInformation.getPrincipal();
        CommonRedisUtils.srem(obtainPrincipalKey(webUserDetails.getUsername()), sessionId);
    }
}
