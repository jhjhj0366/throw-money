package com.project.money.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ThrowMoneyInfo {
    private Long throwAmount;      // 뿌릴 금액
    private Long receiverCount;    // 받을 인원
}
