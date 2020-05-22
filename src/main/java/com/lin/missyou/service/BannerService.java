package com.lin.missyou.service;

import com.lin.missyou.model.dataAccessObject.Banner;
import com.lin.missyou.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {
    @Autowired
    private BannerRepository bannerRepository;

    public Banner getBanner(String name) {
        return bannerRepository.findOneByName(name);
    }
}
