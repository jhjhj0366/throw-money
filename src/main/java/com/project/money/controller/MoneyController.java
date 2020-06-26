package com.project.money.controller;

import com.project.money.dto.ReceiveMoneyDTO;
import com.project.money.dto.ThrowMoneyDTO;
import com.project.money.dto.TransactionDTO;
import com.project.money.model.ThrowMoneyInfo;
import com.project.money.model.Transactions;
import com.project.money.service.MoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/money")
@RequiredArgsConstructor
public class MoneyController {

    private final MoneyService moneyService;

    @PostMapping("/throw")
    public ResponseEntity<ThrowMoneyDTO.Res> setThrowMoneyInfo(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @Valid @RequestBody ThrowMoneyDTO.Req req) {
        log.info("setThrowMoneyInfo - {}, {}, {}", userId, roomId, req);

        String token = moneyService.setThrowMoneyInfo(
                userId,
                roomId,
                ThrowMoneyInfo.builder()
                        .throwAmount(req.getThrowAmount())
                        .receiverCount(req.getReceiverCount()).build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ThrowMoneyDTO.Res.builder().token(token).build());
    }

    @PostMapping("/receive")
    public ResponseEntity<ReceiveMoneyDTO.Res> getReceiveMoneyInfo(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @Valid @RequestBody ReceiveMoneyDTO.Req req) {
        log.info("getReceiveMoney - {}, {}, {}", userId, roomId, req);

        Long receiveAmount = moneyService.getReceiveMoneyInfo(userId, roomId, req.getToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ReceiveMoneyDTO.Res.builder().receiveAmount(receiveAmount).build());
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO.Res> getTransactions(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @Valid @RequestBody TransactionDTO.Req req) {
        log.info("getTransactions - {}, {}, {}", userId, roomId, req);

        Transactions transactions = moneyService.getTransactions(userId, roomId, req.getToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(TransactionDTO.Res
                        .builder()
                        .throwDateTime(transactions.getThrowDateTime())
                        .throwAmount(transactions.getThrowAmount())
                        .receiveTotalAmount(transactions.getReceiveTotalAmount())
                        .receiversInfo(transactions.getReceiversInfo())
                        .build()
                );
    }

}
