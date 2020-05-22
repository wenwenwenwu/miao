package com.lin.missyou.logic;

import com.lin.missyou.exception.exception.ParameterException;
import com.lin.missyou.model.businessObject.SkuOrderBo;
import com.lin.missyou.model.dataAccessObject.OrderSku;
import com.lin.missyou.model.dataAccessObject.Sku;
import com.lin.missyou.model.dataTransferObject.OrderDTO;
import com.lin.missyou.model.dataTransferObject.SkuInfoDTO;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderChecker作用：
 * 前端提供的数据和服务器中的数据校验（有异常立刻抛出，能进入下一步都是通过校验了的）
 * 提供写入数据库的数据
 */
public class OrderChecker {
    private Integer maxSkuLimit;
    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    @Getter //提供数据
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(Integer maxSkuLimit, OrderDTO orderDTO, List<Sku> serverSkuList, CouponChecker couponChecker) {
        this.maxSkuLimit = maxSkuLimit;
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponChecker = couponChecker;
    }

    //提供数据
    public String getLeaderImg(){
        return this.serverSkuList.get(0).getImg();
    }

    //提供数据
    public String getLeaderTitle(){
        return this.serverSkuList.get(0).getTitle();
    }

    //提供数据
    public Integer getTotalCount(){
        return this.orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public void isOK() {
        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBo> skuOrderBoList = new ArrayList<>();
        //校验单品是否删除/下架
        this.skuNotOnSale(this.orderDTO.getSkuInfoList().size(), this.serverSkuList.size());
        for (int i = 0; i < this.serverSkuList.size(); i++) {
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            //校验单品是否售完
            this.containsSoldOutSku(sku);
            //校验单品购买数是否超过库存数
            this.beyondSkuStock(sku, skuInfoDTO);
            //校验单品购买数是否超过最大单品购买数
            this.beyondMaxSkuLimit(skuInfoDTO);
            //为couponChecker校验方法提供数据
            //累加单品总价（sku真实单价*skuInfoDTO购买数）
            serverTotalPrice = serverTotalPrice.add(this.calculateSkuOrderPrice(sku, skuInfoDTO));
            //为couponChecker校验方法提供数据
            //SkuOrderBo对象提供校验有用的数据
            skuOrderBoList.add(new SkuOrderBo(sku, skuInfoDTO));
            //提供数据
            this.orderSkuList.add(new OrderSku(sku,skuInfoDTO));
        }
        //校验前端提交的总价和服务端算出的总价是否一致
        this.totalPriceIsOK(orderDTO.getTotalPrice(),serverTotalPrice);
        if (couponChecker!=null){
            //校验优惠券是否过期
            this.couponChecker.isExpired();
            //校验优惠券能否使用（主要是验证满减/折券的"满"，无门槛券无需校验）
            this.couponChecker.canBeUsed(skuOrderBoList,serverTotalPrice);
            //校验前端提交的最终价和服务端算出的最终价是否一致
            this.couponChecker.isFinalTotalPriceMatch(orderDTO.getFinalTotalPrice(),serverTotalPrice);
        }
    }

    private void skuNotOnSale(int count1, int count2) {
        if (count1 != count1) {
            throw new ParameterException(50002);
        }
    }

    private void containsSoldOutSku(Sku sku) {
        if (sku.getStock() == 0) {
            throw new ParameterException(50001);
        }
    }

    private void beyondSkuStock(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (sku.getStock() < skuInfoDTO.getCount()) {
            throw new ParameterException(50003);
        }
    }

    private void beyondMaxSkuLimit(SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() > this.maxSkuLimit) {
            throw new ParameterException(50004);
        }
    }

    private BigDecimal calculateSkuOrderPrice(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() <= 0) {
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    private void totalPriceIsOK(BigDecimal orderTotalPrice, BigDecimal severTotalPrice) {
        if (orderTotalPrice.compareTo(severTotalPrice) != 0) {
            throw new ParameterException(50005);
        }
    }
}
