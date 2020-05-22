package com.lin.missyou.logic;

import com.lin.missyou.core.enumeration.CouponType;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.exception.exception.ForbiddenException;
import com.lin.missyou.exception.exception.ParameterException;
import com.lin.missyou.model.businessObject.SkuOrderBo;
import com.lin.missyou.model.dataAccessObject.Category;
import com.lin.missyou.model.dataAccessObject.Coupon;
import com.lin.missyou.util.CommonUtil;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CouponChecker {

    private Coupon coupon;
    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon, IMoneyDiscount iMoneyDiscount) {
        this.coupon = coupon;
        this.iMoneyDiscount = iMoneyDiscount;
    }

    public void isExpired() {
        //优惠券是否过期
        Date now = new Date();
        Boolean isInTimeLine = CommonUtil.isInTimeLine(now, this.coupon.getStartTime(), this.coupon.getEndTime());
        if (!isInTimeLine) {
            throw new ForbiddenException(40007);
        }

    }

    public void canBeUsed(List<SkuOrderBo> skuOrderBoList, BigDecimal serverTotalPrice) {
        //优惠券可用的商品总价
        BigDecimal orderCategoryPrice;
        //全场券
        if (coupon.getWholeStore()) {
            orderCategoryPrice = serverTotalPrice;
        } else {
            //优惠券可用的商品类别ID列表
            List<Long> cidList = coupon.getCategoryList()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            //获取订单中优惠券可用类别们的商品总价
            orderCategoryPrice = this.getSumByCategoryList(skuOrderBoList, cidList);
        }
        //校验是否达到"满"的标准
        this.couponCanBeUsed(orderCategoryPrice);
    }

    private BigDecimal getSumByCategoryList(List<SkuOrderBo> skuOrderBoList, List<Long> cidList) {
        return cidList.stream()
                //获取订单中优惠券可用类别的商品总价
                .map(cid -> getSumByCategory(skuOrderBoList, cid))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
    }

    private BigDecimal getSumByCategory(List<SkuOrderBo> skuOrderBoList, Long cid) {
        return skuOrderBoList
                .stream()
                .filter(skuOrderBo -> skuOrderBo.getCategoryId().equals(cid))
                .map(SkuOrderBo::getTotalPrice)
                .reduce(BigDecimal::add) //reduce累加
                .orElse(new BigDecimal("0"));
    }

    public void isFinalTotalPriceMatch(BigDecimal orderFinalTotalPrice, BigDecimal serverTotalPrice) {
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toValue(this.coupon.getType())) {
            case FULL_OFF:
                serverFinalTotalPrice = this.iMoneyDiscount.discount(serverTotalPrice, this.coupon.getRate());
                break;
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                //无门槛减除券要校验减除后是否免费
                if (serverFinalTotalPrice.compareTo(new BigDecimal("0")) <= 0) {
                    throw new ForbiddenException(50011);
                }
                break;
            case FULL_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                break;
            default:
                throw new ParameterException(40009); //不支持的优惠券类型
        }
        //比较前端优惠金额与实际优惠金额
        int compare = serverFinalTotalPrice.compareTo(orderFinalTotalPrice);
        if (compare != 0) {
            throw new ForbiddenException(50008);
        }
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice){
        switch (CouponType.toValue(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                int compare = this.coupon.getFullMoney().compareTo(orderCategoryPrice);
                if (compare > 0) {
                    throw new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }
}
