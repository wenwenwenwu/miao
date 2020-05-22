package com.lin.missyou.model.viewObject;

import com.lin.missyou.model.dataAccessObject.Category;
import com.lin.missyou.model.dataAccessObject.Coupon;

import java.util.ArrayList;
import java.util.List;

public class CouponCategoryVO extends CouponPureVO {
    public List<CategoryPureVO> categories = new ArrayList<>();

    public CouponCategoryVO(Coupon coupon) {
        super(coupon);
        List<Category> categories = coupon.getCategoryList();
        this.categories = CategoryPureVO.getList(categories);
    }

}
