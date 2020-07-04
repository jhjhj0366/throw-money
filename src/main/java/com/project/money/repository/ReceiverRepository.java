package com.project.money.repository;

import com.project.money.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {

    @Query(value = "SELECT SUM (r.receive_amt)" +
            " FROM Receiver r" +
            " WHERE r.transaction_token= :token" +
            " AND r.receive_user_id IS NOT NULL" +
            " GROUP BY r.transaction_token", nativeQuery = true)
    Long findReceiveTotalAmountByTransactionToken(@Param("token") String token);

    Optional<Receiver> findFirstByTransactionTokenAndReceiveUserIdIsNull(String token);

    Optional<Receiver> findByTransactionTokenAndReceiveUserId(String token, Long receiveUserId);

    List<Receiver> findByTransactionTokenAndReceiveUserIdIsNotNull(String token);

}
