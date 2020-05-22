package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByIsRootOrderByIndexAsc(Boolean isRoot);
}
