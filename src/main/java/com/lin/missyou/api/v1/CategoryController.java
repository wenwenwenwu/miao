package com.lin.missyou.api.v1;

import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.model.dataAccessObject.Category;
import com.lin.missyou.model.dataAccessObject.GridCategory;
import com.lin.missyou.model.viewObject.CategoriesAllVO;
import com.lin.missyou.service.CategoryService;
import com.lin.missyou.service.GridCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GridCategoryService gridCategoryService;

    @GetMapping("/all")
    public CategoriesAllVO getAll(){
        Map<Integer, List<Category>> categories = categoryService.getAll();
        return new CategoriesAllVO(categories);
    }

    @GetMapping("/grid/all")
    public List<GridCategory> getGridCategoryList(){
        List<GridCategory> gridCategoryList = gridCategoryService.getGridCategoryList();
        if (gridCategoryList == null){
            throw new NotFoundException(30009);
        }
        return gridCategoryList;
    }

}
