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

    @ManyToOne
    @JoinColumn(name = "transaction_token", referencedColumnName = "token")
    private Transaction transaction;

    @Column(name = "receive_user_id")
    private Long receiveUserId;    // 받은 사람 ID

    @Column(name = "receive_amt", nullable = false)
    private Long receiveAmount;    // 받은 금액

    public void setTransaction(Transaction transaction) {
        if (this.transaction != null) {
            this.transaction.getReceivers().remove(this);
        }
        this.transaction = transaction;
        this.transaction.addRecevers(this);
    }
}