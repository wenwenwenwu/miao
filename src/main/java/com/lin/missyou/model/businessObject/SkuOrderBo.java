package com.lin.missyou.model.businessObject;

import com.lin.missyou.model.dataAccessObject.Sku;
import com.lin.missyou.model.dataTransferObject.SkuInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SkuOrderBo {
    private BigDecimal actualPrice;
    private Integer count;
    private Long categoryId;

    public SkuOrderBo(Sku sku, SkuInfoDTO skuInfoDTO){
        actualPrice = sku.getActualPrice();
        count = skuInfoDTO.getCount();
        categoryId = sku.getCategoryId();
    }

    public BigDecimal getTotalPrice(){
        return actualPrice.multiply(new BigDecimal(count));
    }
}
