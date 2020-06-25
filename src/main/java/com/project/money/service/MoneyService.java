package com.project.money.service;

import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import org.springframework.stereotype.Service;

@Service
public class MoneyService {

    public String setThrowMoneyInfo(ThrowMoneyInfo throwMoneyInfo) {
        return "";
    }

    public Long getReceiveMoneyInfo(Long receiveUserId, String roomId, String token) {
        return 0L;
    }

    public Transactions getTransactions(Long sendUserId, String roomId, String token) {
        return null;
    }

}
