package com.project.money.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

public class ThrowMoneyDTO {

    @Getter
    @ToString
    public static class Req {
        @NotNull
        private Long throwAmount;      // 뿌릴 금액
        @NotNull
        private Long receiverCount;    // 받을 인원
    }

    @Data
    @Builder
    public static class Res {
        private String token;          // 뿌리기시 발급되는 token
    }

}
