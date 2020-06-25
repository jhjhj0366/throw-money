package com.project.money.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Transactions {
    private LocalDateTime throwDateTime;       // 뿌린 시간
    private Long throwAmount;                  // 뿌린 금액
    private Long receiveTotalAmount;           // 받기완료된금액
    private List<ReceiverInfo> receiversInfo;  // 받은 사용자 정보
}
