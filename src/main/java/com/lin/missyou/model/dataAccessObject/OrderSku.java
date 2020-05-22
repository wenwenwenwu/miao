package com.lin.missyou.model.dataAccessObject;

import com.lin.missyou.model.dataTransferObject.SkuInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderSku {
    private Long id;
    private Long spuId;
    private BigDecimal finalPrice; //singlePrice*count
    private BigDecimal singlPrice;
    private List<String> specValues;
    private Integer count;
    private String img;
    private String title;

    public OrderSku(Sku sku, SkuInfoDTO skuInfoDTO){
        this.id = sku.getId();
        this.spuId = sku.getSpuId();
        this.finalPrice = sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
        this.singlPrice = sku.getActualPrice();
        this.count = skuInfoDTO.getCount();
        this.img = sku.getImg();
        this.title = sku.getTitle();
        this.specValues = sku.getSpecValueList();
    }
}
