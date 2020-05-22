package com.lin.missyou.model.viewObject;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.model.dataAccessObject.Category;
import com.lin.missyou.model.dataAccessObject.Coupon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
//避免实体类双向循环序列化
public class CategoryPureVO {
    @Id
    private Long id;
    private String name;
    private Boolean isRoot;
    private Long parentId;
    private String img;
    private Long index;

    public CategoryPureVO(Category category){
//        方法1
        BeanUtils.copyProperties(category,this);
//        方法2
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        mapper.map(category,this);
    }

    public static List<CategoryPureVO> getList(List<Category> categoryList) {
        return categoryList.stream()
                .map(CategoryPureVO::new)
                .collect(Collectors.toList());
    }
}
