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
import java.util.concurrent.TimeUnit;

public class RedisTokenRepositoryImpl implements PersistentTokenRepository {
    private static final String REMEMBER_ME_TOKEN_PREFIX = "_remember_me_token_";
    private static final String REMEMBER_ME_SERIES_PREFIX = "_remember_me_series_";
    public static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;

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
        String tokenKey = obtainRememberMeKey(series);
        String seriesKey = obtainRememberMeSeriesKey(username);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("username", username);
        tokenMap.put("series", series);
        tokenMap.put("token", token.getTokenValue());
        tokenMap.put("last_used", CustomDateUtils.format(token.getDate(), Constants.DEFAULT_DATE_PATTERN));

        CommonRedisUtils.hmset(tokenKey, tokenMap);
        CommonRedisUtils.expire(tokenKey, TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
        CommonRedisUtils.sadd(seriesKey, tokenKey);
        CommonRedisUtils.expire(seriesKey, TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        String tokenKey = obtainRememberMeKey(series);
        Map<String, String> tokenMap = CommonRedisUtils.hgetAll(tokenKey);

        tokenMap.put("token", tokenValue);
        tokenMap.put("last_used", CustomDateUtils.format(lastUsed, Constants.DEFAULT_DATE_PATTERN));

        CommonRedisUtils.hmset(tokenKey, tokenMap);
        CommonRedisUtils.expire(tokenKey, TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
        CommonRedisUtils.expire(obtainRememberMeSeriesKey(tokenMap.get("username")), TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
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
        String seriesKey = obtainRememberMeSeriesKey(username);
        Set<String> deleteKeys = CommonRedisUtils.smembers(seriesKey);
        deleteKeys.add(seriesKey);
        CommonRedisUtils.del(deleteKeys.toArray(new String[]{}));
    }
}
