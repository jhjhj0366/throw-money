package com.project.money.dto;

import com.project.money.model.ReceiverInfo;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDTO {

    @Getter
    @ToString
    public static class Req {
        @NotNull
        @Size(min = 3, max = 3)
        private String token;          // 뿌리기시 발급되는 token
    }

    @Data
    @Builder
    public static class Res {
        private LocalDateTime throwDateTime;       // 뿌린 시간
        private Long throwAmount;                  // 뿌린 금액
        private Long receiveTotalAmount;           // 받기완료된금액
        private List<ReceiverInfo> receiversInfo;  // 받은 사용자 정보
    }

}
