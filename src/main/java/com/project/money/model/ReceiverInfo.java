package com.project.money.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ReceiverInfo {
    private Long receiveUserId;    // 받은 사람 ID
    private Long receiveAmount;    // 받은 금액
}
