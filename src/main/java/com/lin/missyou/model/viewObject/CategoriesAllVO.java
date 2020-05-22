package com.lin.missyou.model.viewObject;

import com.lin.missyou.model.dataAccessObject.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoriesAllVO {
    private List<CategoryPureVO>roots;
    private List<CategoryPureVO>subs;

    public CategoriesAllVO(Map<Integer,List<Category>> map){
        //函数式编程stream流的应用
        //构造方法引用CategoryPureVO::new
        this.roots = CategoryPureVO.getList(map.get(1));
       this.subs = CategoryPureVO.getList(map.get(2));
    }
}
