package com.lin.missyou.model.viewObject;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


public class DozerPaging<RawClass, Target> extends Paging {

    @SuppressWarnings("unchecked")
    public DozerPaging(Page<RawClass> rawPage, Class<Target> targetClass) {
        this.initPageParameters(rawPage);
        List<RawClass> rawList = rawPage.getContent();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<Target> targetList = new ArrayList<>();
        rawList.forEach(rawItem -> {
            Target targetItem = mapper.map(rawItem, targetClass);
            targetList.add(targetItem);
        });
        this.setItems(targetList);
    }

}


