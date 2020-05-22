package com.lin.missyou.model.viewObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Paging<RawClass> {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private long totalSize;
    private List<RawClass> items;

    public Paging(Page<RawClass> page){
        this.initPageParameters(page);
        this.items = page.getContent();
    }

    void initPageParameters(Page<RawClass> page){
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalSize = page.getTotalElements();
        this.totalPage = page.getTotalPages();
    }
}
