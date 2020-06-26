package com.project.money.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 3, nullable = false)
    private String token;          // 뿌리기시 발급되는 token

    @Column(name = "receive_user_id", nullable = false)
    private Long receiveUserId;    // 받은 사람 ID

    @Column(name = "receive_amt", nullable = false)
    private Long receiveAmount;    // 받은 금액

}
