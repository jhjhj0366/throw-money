package com.project.money.service;

import com.project.money.entity.Receiver;
import com.project.money.entity.Transaction;
import com.project.money.exception.BusinessException;
import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import com.project.money.repository.ReceiverRepository;
import com.project.money.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MoneyServiceTest {

    @Autowired
    MoneyService moneyService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ReceiverRepository receiverRepository;

    private ThrowMoneyInfo originThrowMoneyInfo;
    private Transaction originTransaction;
    private Receiver originReceiver;

    private String originToken;

    @Rule
    public ExpectedException moneyException = ExpectedException.none();

    @Before
    public void setup() {

        originToken = moneyService.getToken();

        originThrowMoneyInfo =
                ThrowMoneyInfo
                        .builder()
                        .throwAmount(5000L)
                        .receiverCount(5L)
                        .build();

        originTransaction =
                Transaction
                        .builder()
                        .sendUserId(1L)
                        .roomId("R1")
                        .token(originToken)
                        .throwAmount(originThrowMoneyInfo.getThrowAmount())
                        .throwDateTime(LocalDateTime.now())
                        .receiverCount(originThrowMoneyInfo.getReceiverCount())
                        .build();

        originReceiver = Receiver
                .builder()
                .token(originToken)
                .receiveUserId(999L)
                .receiveAmount(5000L)
                .build();

        originTransaction = transactionRepository.save(originTransaction);
        originReceiver = receiverRepository.save(originReceiver);
    }

    @Test
    public void 돈_뿌리기_info() {

        String token = moneyService.getToken();

        Transaction transaction =
                Transaction
                        .builder()
                        .sendUserId(2L)
                        .roomId("R2")
                        .token(token)
                        .throwAmount(3000L)
                        .throwDateTime(LocalDateTime.now())
                        .receiverCount(3L)
                        .build();

        Assert.assertNotNull(moneyService.setThrowMoneyInfo(transaction.getSendUserId(), transaction.getRoomId(), originThrowMoneyInfo));
        moneyException.expect(BusinessException.class);
    }

    // 돈 받기
    @Test
    public void 돈_받기_info() {

        Assert.assertNotNull(moneyService.getReceiveMoneyInfo(originReceiver.getReceiveUserId(), originTransaction.getRoomId(), originToken));
        moneyException.expect(BusinessException.class);
    }

    // 돈 조회
    @Test
    public void 돈_조회() {

        Transactions transactions = moneyService.getTransactions(originTransaction.getSendUserId(), originTransaction.getRoomId(), originToken);
        Assert.assertNotNull(transactions);
    }
}
