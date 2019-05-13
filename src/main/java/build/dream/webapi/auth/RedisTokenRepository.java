package build.dream.webapi.auth;

import build.dream.common.utils.CommonRedisUtils;
import build.dream.common.utils.CustomDateUtils;
import build.dream.webapi.constants.Constants;
import org.apache.commons.collections.MapUtils;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisTokenRepository implements PersistentTokenRepository {
    private static final String REMEMBER_ME_TOKEN_PREFIX = "_remember_me_token_";
    private static final String REMEMBER_ME_SERIES_PREFIX = "_remember_me_series_";

    private String obtainRememberMeKey(String series) {
        return REMEMBER_ME_TOKEN_PREFIX + series;
    }

    private String obtainRememberMeSeriesKey(String username) {
        return REMEMBER_ME_SERIES_PREFIX + username;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String series = token.getSeries();
        String username = token.getUsername();
        String key = obtainRememberMeKey(series);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("username", username);
        tokenMap.put("series", series);
        tokenMap.put("token", token.getTokenValue());
        tokenMap.put("last_used", CustomDateUtils.format(token.getDate(), Constants.DEFAULT_DATE_PATTERN));

        CommonRedisUtils.hmset(key, tokenMap);
        CommonRedisUtils.sadd(obtainRememberMeSeriesKey(username), key);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        String key = obtainRememberMeKey(series);
        Map<String, String> tokenMap = CommonRedisUtils.hgetAll(key);

        tokenMap.put("token", tokenValue);
        tokenMap.put("last_used", CustomDateUtils.format(lastUsed, Constants.DEFAULT_DATE_PATTERN));

        CommonRedisUtils.hmset(obtainRememberMeKey(series), tokenMap);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Map<String, String> tokenMap = CommonRedisUtils.hgetAll(obtainRememberMeKey(seriesId));
        if (MapUtils.isNotEmpty(tokenMap)) {
            String username = tokenMap.get("username");
            String series = tokenMap.get("series");
            String token = tokenMap.get("token");
            Date lastUsed = CustomDateUtils.parse(tokenMap.get("last_used"), Constants.DEFAULT_DATE_PATTERN);
            return new PersistentRememberMeToken(username, series, token, lastUsed);
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        String key = obtainRememberMeSeriesKey(username);
        Set<String> keys = CommonRedisUtils.smembers(key);
        keys.add(key);
        CommonRedisUtils.del(keys.toArray(new String[]{}));
    }
}
