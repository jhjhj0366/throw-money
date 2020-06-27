package com.project.money.service;

import com.project.money.config.CacheType;
import com.project.money.entity.Receiver;
import com.project.money.entity.Transaction;
import com.project.money.exception.BusinessException;
import com.project.money.exception.ErrorCode;
import com.project.money.model.ReceiverInfo;
import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import com.project.money.repository.ReceiverRepository;
import com.project.money.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoneyService {
    private static final int TOKEN_SIZE = 3;

    private final CacheManager cacheManager;
    private final TransactionRepository transactionRepository;
    private final ReceiverRepository receiverRepository;

    private final Cache cache = cacheManager.getCache(CacheType.THROW_MONEY_TOKEN.getCacheName());
    private static final Random random = new Random();

    @Transactional
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

        transactionRepository.save(transaction);

        // save receiver
        Long throwAmount = throwMoneyInfo.getThrowAmount();
        for (int i = 0; i < throwMoneyInfo.getReceiverCount() - 1; i++) {
            long receiveAmount = throwAmount * random.nextInt(70) / 100;

            receiverRepository.save(Receiver.builder()
                    .token(token)
                    .receiveAmount(receiveAmount)
                    .build());

            throwAmount = throwAmount - receiveAmount;
        }

        receiverRepository.save(Receiver.builder()
                .token(token)
                .receiveAmount(throwAmount)
                .build());

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
        return transactionRepository.findByToken(token)
                .map(transactionInfo -> {
                    // check receiver
                    if (receiveUserId.equals(transactionInfo.getSendUserId()) || !roomId.equals(transactionInfo.getRoomId())) {
                        throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, "돈 받기를 할 수 없는 사용자 입니다.");
                    }

                    // update receiver
                    return transactionInfo.getReceivers().stream()
                            .filter(r -> Objects.isNull(r.getReceiveUserId()))
                            .findAny()
                            .map(receiverInfo -> {
                                receiverRepository.findById(receiverInfo.getId())
                                        .map(receiver -> {
                                            receiver.setReceiveUserId(receiveUserId);
                                            return receiverRepository.save(receiver);
                                        })
                                        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "DB ERROR."));
                                return receiverInfo.getReceiveAmount();
                            })
                            .orElseThrow(() -> new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, "더이상 받을 수 있는 돈이 없습니다."));
                })
                .orElseThrow(() -> new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, "돈 받기를 할 수 없는 사용자 입니다."));
    }

    public Transactions getTransactions(Long sendUserId, String roomId, String token) {
        final LocalDateTime startDate = LocalDateTime.of(LocalDate.from(LocalDateTime.now().minusDays(6)), LocalTime.of(0, 0, 0));
        final LocalDateTime endDate = LocalDateTime.now();

        return transactionRepository.findByTokenAndSendUserIdAndThrowDateTimeBetween(token, sendUserId, startDate, endDate)
                .map(transaction -> Transactions.builder()
                        .throwDateTime(transaction.getThrowDateTime())
                        .throwAmount(transaction.getThrowAmount())
                        .receiveTotalAmount(receiverRepository.findReceiveTotalAmountByToken(token))
                        .receiversInfo(transaction.getReceivers()
                                .stream()
                                .filter(receiver -> Objects.nonNull(receiver.getReceiveUserId()))
                                .map(receiver -> ReceiverInfo.builder()
                                        .receiveUserId(receiver.getReceiveUserId())
                                        .receiveAmount(receiver.getReceiveAmount())
                                        .build())
                                .collect(Collectors.toList())
                        )
                        .build())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "조회할 수 없는 정보입니다."));
    }

    // get token function
    String getToken() {
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
    static String generateToken() {
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
