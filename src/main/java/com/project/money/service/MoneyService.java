package com.project.money.service;

import com.project.money.config.CacheType;
import com.project.money.entity.Transaction;
import com.project.money.exception.BusinessException;
import com.project.money.exception.ErrorCode;
import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import com.project.money.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MoneyService {
    private static final int TOKEN_SIZE = 3;

    private final CacheManager cacheManager;
    private final TransactionRepository transactionRepository;

    private final Cache cache = cacheManager.getCache(CacheType.THROW_MONEY_TOKEN.getCacheName());


    public String setThrowMoneyInfo(Long sendUserId, String roomId, ThrowMoneyInfo throwMoneyInfo) {

        // get token
        final String token = getToken();

        // save transaction
        final Transaction transaction = Transaction.builder()
                .sendUserId(sendUserId)
                .roomId(roomId)
                .token(token)
                .throwAmount(throwMoneyInfo.getThrowAmount())
                .receiverCount(throwMoneyInfo.getReceiverCount())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        // save receiver
        // TODO : 금액 분배 로직 필요

        return token;
    }

    public Long getReceiveMoneyInfo(Long receiveUserId, String roomId, String token) {

        // check token
        if (Objects.isNull(cache)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "캐시 에러 입니다.");
        }

        if (! cache.evictIfPresent(token)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_VALUE);
        }

        // get transaction
        Optional<Transaction> transaction = transactionRepository.findTopByToken(token);

        // check receiver
        transaction
                .map(transactionInfo -> {
                    if (receiveUserId.equals(transactionInfo.getSendUserId()) || !roomId.equals(transactionInfo.getRoomId())) {
                        throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, "돈 받기를 할 수 없는 사용자 입니다.");
                    }
                    return transactionInfo.getReceivers().stream()
                            .peek(receiver -> {
                                if (Objects.nonNull(receiver.getReceiveUserId())) {

                                }
                            });
                });

        // update receiver
        return 0L;
    }

    public Transactions getTransactions(Long sendUserId, String roomId, String token) {
        return null;
    }

    // get token function
    String getToken() {
        if (Objects.isNull(cache)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Cache Error.");
        }

        final String token = generateToken();

        // check token
        // TODO : Transaction DB 조회 로직으로 변경
        if (cache.evictIfPresent(token)) {
            return getToken();
        }

        // cache token
        cache.put(token, token);

        return token;
    }

    // make token function
    static String generateToken() {
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
