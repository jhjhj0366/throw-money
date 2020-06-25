package com.project.money.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Receiver {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receive_user_id", nullable = false)
    private Long receiveUserId;    // 받은 사람 ID

    @Column(name = "receive_amt", nullable = false)
    private Long receiveAmt;       // 받은 사람 금액

}
