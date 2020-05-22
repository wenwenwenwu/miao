package com.lin.missyou.repository;

import com.lin.missyou.model.dataAccessObject.Banner;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BannerRepository extends JpaRepository<Banner, Long> {
    Banner findOneByName(String name);
}
