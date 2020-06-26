package com.project.money.service;

import com.project.money.config.CacheType;
import com.project.money.exception.BusinessException;
import com.project.money.exception.ErrorCode;
import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MoneyService {
    private static final int TOKEN_SIZE = 3;

    private final CacheManager cacheManager;

    public String setThrowMoneyInfo(ThrowMoneyInfo throwMoneyInfo) {

        // get token
        final String token = getToken();

        // save transaction

        // save receiver

        return token;
    }

    public Long getReceiveMoneyInfo(Long receiveUserId, String roomId, String token) {

        // check token

        // get id

        // check receiver

        // update receiver

        return 0L;
    }

    public Transactions getTransactions(Long sendUserId, String roomId, String token) {
        return null;
    }

    // get token function
    private String getToken() {
        Cache cache = cacheManager.getCache(CacheType.THROW_MONEY_TOKEN.getCacheName());
        if (Objects.isNull(cache)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Cache Error.");
        }

        final String token = generateToken();

        // check token
        if (cache.evictIfPresent(token)) {
            return getToken();
        }

        // cache token
        cache.put(token, token);

        return token;
    }

    // make token function
    private static String generateToken() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < TOKEN_SIZE; i++) {
            switch (random.nextInt(3)) {
                case 0:
                    buffer.append((char)(random.nextInt(26) + 97)); // a to z
                    break;
                case 1:
                    buffer.append((char)(random.nextInt(26) + 65)); // A to Z
                    break;
                case 2:
                    buffer.append((char)(random.nextInt(10)));      // 0 to 9
                    break;
            }
        }

        return String.valueOf(buffer);
    }

}
