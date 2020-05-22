package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//操作数据库
//SpuRepository扩展了JpaRepository接口。接口无需添加到容器
//JpaRepository的范型参数分别为操作的数据表对应的实体类型及其主键类型
//SpuRepository可以通过现成的接口方法或自定义的接口方法来调用特定的SQL语句。自定义接口方法写在SpuRepository接口中，要遵守接口方法命名规则
public interface SpuRepository extends JpaRepository<Spu, Long> {
    Spu findOneById(Long id);

    Page<Spu> findByRootCategoryIdOrderByCreateTimeDesc(Long cid, Pageable pageable);

    Page<Spu> findByCategoryIdOrderByCreateTimeDesc(Long cid, Pageable pageable);

}
