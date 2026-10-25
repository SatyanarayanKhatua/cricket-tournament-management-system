package com.cricket.tournament.repository;

import com.cricket.tournament.entity.BallDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BallDeliveryRepository
        extends JpaRepository<BallDelivery, Long> {

    List<BallDelivery> findByInningsIdOrderByDeliveryNumberAsc(
            Long inningsId
    );

    Optional<BallDelivery> findTopByInningsIdOrderByDeliveryNumberDesc(
            Long inningsId
    );

    boolean existsByInningsIdAndDeliveryNumber(
            Long inningsId,
            int deliveryNumber
    );
    boolean existsByInningsIdAndDeliveryNumberGreaterThan(
            Long inningsId, int deliveryNumber);
    long countByInningsId(Long inningsId);
}