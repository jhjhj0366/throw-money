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

    @Query(value = "SELECT SUM (r.receiveAmount)" +
            " FROM Receiver r" +
            " WHERE r.token = :token" +
            " AND r.receiveUserId IS NOT NULL" +
            " GROUP BY r.token")
    Long findReceiveTotalAmountByToken(@Param("token") String token);

    Optional<Receiver> findFirstByTokenAndReceiveUserIdIsNull(String token);

    Optional<Receiver> findByTokenAndReceiveUserId(String token, Long receiveUserId);

    List<Receiver> findByTokenAndReceiveUserIdIsNotNull(String token);

}
