package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByExpiredTimeGreaterThanAndStatusAndUserId(Date now, int status, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long uid, int status, Pageable pageable);

    Optional<Order> findFirstByUserIdAndId(Long uid, Long id);

    Optional<Order> findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status =:status " +
            "where o.orderNo=:orderNo\n")
    int updateStatusByOrderNo(String orderNo, Integer status);

    @Modifying
    @Query("update Order o set o.status = 5 where o.status = 1 and o.id = :oid")
    int cancelOrder(Long oid);
}
