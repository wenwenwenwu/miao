package com.lin.missyou.service;

import com.lin.missyou.model.dataAccessObject.Spu;
import com.lin.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

//处理业务逻辑
@Service
public class SpuService {
    @Autowired
    private SpuRepository spuRepository;

    public Spu getSpu(Long id) {
        return this.spuRepository.findOneById(id);
    }

    public List<Spu> getAll(){
        //调用现成的方法
        return this.spuRepository.findAll();
    }

    public Page<Spu> getPagingSpu(Integer pageNum, Integer pageSize) {
        //创建分页请求对象
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        //返回Page<Spu>对象，包含查询到的数据和分页信息
        return this.spuRepository.findAll(pageable);
    }

    public Page<Spu> getPagingSpu(Long cid, Boolean isRoot, Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        if (isRoot) {
            return this.spuRepository.findByRootCategoryIdOrderByCreateTimeDesc(cid, pageable);
        } else {
            return this.spuRepository.findByCategoryIdOrderByCreateTimeDesc(cid, pageable);
        }
    }
}
