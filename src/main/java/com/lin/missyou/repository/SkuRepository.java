package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku,Long> {

    List<Sku> findAllByIdIn(List<Long>ids);

    @Modifying //改变（更新/插入/删除）数据库
    @Query("update Sku s set s.stock = s.stock - :quantity\n" +
            "where s.id = :skuId\n" +
            "and s.stock >= :quantity\n")
    int reduceStock(Long skuId, Long quantity);

    @Modifying
    @Query("update Sku s set s.stock = s.stock +(:quantity) where s.id = :sid")
    int recoverStock(@Param("sid") Long sid,
                     @Param("quantity") Long quantity);

}
